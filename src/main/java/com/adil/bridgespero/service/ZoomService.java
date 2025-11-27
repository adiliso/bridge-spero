package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.entity.TeacherEntity;
import com.adil.bridgespero.domain.model.dto.request.ZoomCreateMeetingRequest;
import com.adil.bridgespero.domain.model.dto.response.ZoomCreateMeetingResponse;
import com.adil.bridgespero.domain.model.dto.response.ZoomTokenResponse;
import com.adil.bridgespero.domain.repository.TeacherRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ZoomService {

    final RestTemplate restTemplate = new RestTemplate();
    final TeacherRepository teacherRepository;
    final TeacherService teacherService;

    @Value("${zoom.client-id}")
    String clientId;

    @Value("${zoom.client-secret}")
    String clientSecret;

    @Value("${zoom.redirect-uri}")
    String redirectUri;

    public URI buildAuthorizationUri(Long teacherId) {
        String url = "https://zoom.us/oauth/authorize?response_type=code&client_id=" + clientId
                     + "&redirect_uri=" + redirectUri
                     + "&state=" + teacherId;
        return URI.create(url);
    }

    public void exchangeCodeForTokens(String code, Long teacherId) {
        String tokenUrl = "https://zoom.us/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("redirect_uri", redirectUri);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<ZoomTokenResponse> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, ZoomTokenResponse.class);
        teacherService.saveToken(response.getBody(), teacherId);
    }


    public ZoomTokenResponse refreshAccessToken(String refreshToken) {
        String tokenUrl = "https://zoom.us/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<ZoomTokenResponse> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, ZoomTokenResponse.class);
        return response.getBody();
    }


    public ZoomCreateMeetingResponse createInstantMeeting(Long teacherId) {

        TeacherEntity teacher = teacherService.getTeacher(teacherId);
        ensureValidAccessToken(teacher);

        String url = "https://api.zoom.us/v2/users/me/meetings";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(teacher.getZoomAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        ZoomCreateMeetingRequest requestBody = new ZoomCreateMeetingRequest(
                "Lesson with " + teacher.getName(),
                1, // instant meeting
                true
        );

        HttpEntity<ZoomCreateMeetingRequest> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<ZoomCreateMeetingResponse> response =
                restTemplate.postForEntity(url, request, ZoomCreateMeetingResponse.class);

        return response.getBody();
    }


    private void ensureValidAccessToken(TeacherEntity teacher) {
        LocalDateTime expiry = teacher.getZoomTokenExpiry();
        if (expiry == null || expiry.isBefore(LocalDateTime.now(ZoneOffset.UTC).plusSeconds(30))) {
// refresh
            ZoomTokenResponse refreshed = refreshAccessToken(teacher.getZoomRefreshToken());
            teacher.setZoomAccessToken(refreshed.getAccessToken());
            teacher.setZoomRefreshToken(refreshed.getRefreshToken());
            teacher.setZoomTokenExpiry(LocalDateTime.now(ZoneOffset.UTC).plusSeconds(refreshed.getExpiresIn()));
            teacherRepository.save(teacher);
        }
    }
}
