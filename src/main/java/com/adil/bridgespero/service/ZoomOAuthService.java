package com.adil.bridgespero.service;

import com.adil.bridgespero.config.properties.ZoomProperties;
import com.adil.bridgespero.domain.model.dto.TokenPair;
import com.adil.bridgespero.domain.repository.StateRedisRepository;
import com.adil.bridgespero.domain.repository.ZoomTokenRedisRepository;
import com.adil.bridgespero.exception.StateNotFoundException;
import com.adil.bridgespero.exception.UserNotConnectedToZoomException;
import com.adil.bridgespero.exception.ZoomOAuthCallbackProcessingException;
import com.adil.bridgespero.exception.ZoomOAuthTokenExchangeFailedException;
import com.adil.bridgespero.exception.ZoomTokenRefreshFailedException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ZoomOAuthService {

    private final ZoomProperties props;
    private final ZoomTokenRedisRepository zoomTokenRedisRepository;
    private final StateRedisRepository stateRedisRepository;
    private final HttpClient httpClient;
    private final ObjectMapper mapper = new ObjectMapper();

    private static final String ZOOM_AUTH_URL = "https://zoom.us/oauth/authorize";
    private static final String ZOOM_TOKEN_URL = "https://zoom.us/oauth/token";

    public String buildAuthorizationUrl(String username) {
        String state = UUID.randomUUID().toString();

        stateRedisRepository.save(state, username);
        return ZOOM_AUTH_URL
               + "?response_type=code"
               + "&client_id=" + props.getClientId()
               + "&redirect_uri=" + URLEncoder.encode(props.getRedirectUri(), StandardCharsets.UTF_8)
               + "&state=" + state;
    }

    public void handleOAuthCallback(String code, String state) {
        try {
            String credentials = props.getClientId() + ":" + props.getClientSecret();
            String encodedAuth = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
            String username = stateRedisRepository.read(state);

            if (state == null) {
                throw new StateNotFoundException();
            }

            String url = ZOOM_TOKEN_URL
                         + "?grant_type=authorization_code"
                         + "&code=" + code
                         + "&redirect_uri=" + URLEncoder.encode(props.getRedirectUri(), StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Basic " + encodedAuth)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new ZoomOAuthCallbackProcessingException(response.statusCode(), response.body());
            }

            JsonNode json = mapper.readTree(response.body());

            String accessToken = json.get("access_token").asText();
            String refreshToken = json.get("refresh_token").asText();
            int expiresIn = json.get("expires_in").asInt();
            long expiresAt = System.currentTimeMillis() / 1000 + expiresIn - 30;

            TokenPair tokenPair = TokenPair.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .expiresAt(expiresAt)
                    .build();

            zoomTokenRedisRepository.save(username, tokenPair);
            stateRedisRepository.delete(state);
        } catch (Exception e) {
            throw new RuntimeException("Failed to handle Zoom OAuth callback", e);
        }
    }

    public TokenPair refreshToken(String username) {
        try {
            TokenPair old = zoomTokenRedisRepository.read(username);

            if (old == null) {
                throw new UserNotConnectedToZoomException(username);
            }

            String url = ZOOM_TOKEN_URL
                         + "?grant_type=refresh_token"
                         + "&refresh_token=" + old.getRefreshToken();

            String credentials = props.getClientId() + ":" + props.getClientSecret();
            String encodedAuth = Base64.getEncoder()
                    .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Basic " + encodedAuth)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new ZoomTokenRefreshFailedException(response.statusCode(), response.body());
            }

            JsonNode json = mapper.readTree(response.body());

            TokenPair newPair = TokenPair.builder()
                    .accessToken(json.get("access_token").asText())
                    .refreshToken(json.get("refresh_token").asText())
                    .expiresAt(System.currentTimeMillis() / 1000
                               + json.get("expires_in").asInt() - 30)
                    .build();

            zoomTokenRedisRepository.save(username, newPair);
            return newPair;

        } catch (UserNotConnectedToZoomException | ZoomTokenRefreshFailedException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to refresh Zoom token due to internal/network error.", e);
        }
    }

    public String getValidAccessToken(String username) {

        TokenPair pair = zoomTokenRedisRepository.read(username);

        if (pair == null) {
            throw new UserNotConnectedToZoomException(username);
        }

        long now = System.currentTimeMillis() / 1000;

        if (pair.getExpiresAt() <= now) {
            pair = refreshToken(username);
        }

        return pair.getAccessToken();
    }

    public JsonNode zoomGet(String username, String endpoint) {
        try {
            String token = getValidAccessToken(username);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.zoom.us/v2/" + endpoint))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new ZoomOAuthTokenExchangeFailedException(response.statusCode(), response.body());
            }

            return mapper.readTree(response.body());

        } catch (Exception e) {
            throw new RuntimeException("Zoom API GET failed", e);
        }
    }

    public JsonNode getZoomProfile(String username) {
        return zoomGet(username, "users/me");
    }

}
