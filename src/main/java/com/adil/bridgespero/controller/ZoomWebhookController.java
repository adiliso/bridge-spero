package com.adil.bridgespero.controller;

import com.adil.bridgespero.service.ZoomWebhookService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/zoom/webhook")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ZoomWebhookController {

    ZoomWebhookService webhookService;

    @PostMapping
    public ResponseEntity<?> handleWebhook(
            @RequestHeader(value = "X-Zoom-Header-Timestamp", required = false) String timestamp,
            @RequestHeader(value = "X-Zoom-Signature", required = false) String signature,
            @RequestBody Map<String, Object> body
    ) {

        String event = (String) body.get("event");

        // 🚨 Do NOT validate signature for endpoint validation request
        if (!"endpoint.url_validation".equals(event)) {
            webhookService.validateRequest(timestamp, signature, body);
        }

        // Process and return response
        return ResponseEntity.ok(webhookService.processEvent(body));
    }
}