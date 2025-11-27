package com.adil.bridgespero.controller;

import com.adil.bridgespero.security.model.CustomUserPrincipal;
import com.adil.bridgespero.service.ZoomOAuthService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/zoom/oauth")
@RequiredArgsConstructor
public class ZoomOAuthController {

    private final ZoomOAuthService service;

    @GetMapping("/authorize")
    public String authorize(@AuthenticationPrincipal CustomUserPrincipal user) {
        return "redirect:" + service.buildAuthorizationUrl(user.getUsername());
    }

    @GetMapping("/callback")
    public String callback(@RequestParam String code,
                           @RequestParam String state) {
        service.handleOAuthCallback(code, state);
        return "Zoom connected successfully";
    }

    @GetMapping("/verify")
    public JsonNode verify(String email) {
        return service.getZoomProfile(email);
    }
}
