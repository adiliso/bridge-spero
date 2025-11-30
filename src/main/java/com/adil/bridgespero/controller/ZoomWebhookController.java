package com.adil.bridgespero.controller;

import com.adil.bridgespero.service.ZoomWebhookService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/zoom/webhook")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ZoomWebhookController {

    ZoomWebhookService webhookService;

    @PostMapping
    public Map<String, String> handleWebhook(@RequestBody Map<String, Object> body) {
        return webhookService.processEvent(body);
    }
}
