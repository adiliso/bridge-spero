package com.adil.bridgespero.controller;

import com.adil.bridgespero.security.model.CustomUserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping
    public String health(@AuthenticationPrincipal CustomUserPrincipal user) {
        return user.getUsername();
    }
}
