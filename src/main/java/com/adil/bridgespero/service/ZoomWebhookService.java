package com.adil.bridgespero.service;

import com.adil.bridgespero.config.properties.ZoomProperties;
import com.adil.bridgespero.util.JsonUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ZoomWebhookService {

    ZoomProperties zoomProperties;
    GroupService groupService; // your existing service

    // ------------------------------
    // 🔐 Validate Zoom Webhook Signature
    // ------------------------------
    public void validateRequest(String timestamp, String signature, Map<String, Object> body) {
        if (timestamp == null || signature == null) {
            throw new SecurityException("Missing Zoom signature headers");
        }

        String message = timestamp + "." + JsonUtil.toJson(body);

        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec key = new SecretKeySpec(
                    zoomProperties.getWebhookSecret().getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            );
            mac.init(key);
            String expectedHash = Base64.getEncoder()
                    .encodeToString(mac.doFinal(message.getBytes(StandardCharsets.UTF_8)));

            String expected = "v0=" + expectedHash;

            if (!expected.equals(signature)) {
                throw new SecurityException("Invalid Zoom Webhook signature");
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to validate Zoom webhook", e);
        }
    }

    // ------------------------------
    // 🔔 Handle Zoom Events
    // ------------------------------
    public Map<String, String> processEvent(Map<String, Object> body) {
        String event = (String) body.get("event");

        switch (event) {
            case "endpoint.url_validation":
                return handleUrlValidation(body);

            case "meeting.ended":
                handleMeetingEnded(body);
                break;
        }

        return Map.of("status", "ok");
    }

    // ------------------------------
    // 🔐 Endpoint Validation
    // ------------------------------
    private Map<String, String> handleUrlValidation(Map<String, Object> body) {
        Map<String, Object> payload = (Map<String, Object>) body.get("payload");
        String plainToken = (String) payload.get("plainToken");

        String encryptedToken = encryptToken(plainToken);

        return Map.of(
                "plainToken", plainToken,
                "encryptedToken", encryptedToken
        );
    }

    // ------------------------------
    // 🔥 Meeting Ended Handler
    // ------------------------------
    private void handleMeetingEnded(Map<String, Object> body) {
        Map<String, Object> payload = (Map<String, Object>) body.get("payload");
        Map<String, Object> object = (Map<String, Object>) payload.get("object");

        String meetingId = object.get("id").toString(); // safer than Long

        groupService.endLessonByZoomMeetingId(meetingId);
    }

    // ------------------------------
    // 🔑 HMAC Token Encryption for Validation
    // ------------------------------
    private String encryptToken(String plainToken) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(
                    zoomProperties.getWebhookSecret().getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            );
            mac.init(keySpec);
            byte[] rawHmac = mac.doFinal(plainToken.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(rawHmac);

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate Zoom HMAC token", e);
        }
    }
}
