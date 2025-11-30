package com.adil.bridgespero.service;

import com.adil.bridgespero.config.properties.ZoomProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ZoomWebhookService {

    ZoomProperties zoomProperties;
    GroupService groupService;

    public Map<String, String> processEvent(Map<String, Object> body) {
        String event = (String) body.get("event");

        if ("endpoint.url_validation".equals(event)) {
            Map<String, Object> payload = (Map<String, Object>) body.get("payload");
            String plainToken = (String) payload.get("plainToken");

            String encryptedToken = encryptToken(plainToken);

            return Map.of(
                    "plainToken", plainToken,
                    "encryptedToken", encryptedToken
            );
        }

        if ("meeting.ended".equals(event)) {
            handleMeetingEnded(body);
        }
        return Map.of();
    }

    private void handleMeetingEnded(Map<String, Object> body) {
        Map<String, Object> payload = (Map<String, Object>) body.get("payload");
        Map<String, Object> object = (Map<String, Object>) payload.get("object");

        Long meetingId = Long.valueOf(object.get("id").toString());

        groupService.endLessonByZoomMeetingId(meetingId);
    }

    private String encryptToken(String plainToken) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");

            SecretKeySpec keySpec = new SecretKeySpec(
                    zoomProperties.getWebhookSecret().getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            );

            mac.init(keySpec);
            byte[] rawHmac = mac.doFinal(plainToken.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(rawHmac);

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate Zoom HMAC token", e);
        }
    }
}
