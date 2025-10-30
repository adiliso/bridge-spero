package com.adil.bridgespero.controller;

import com.adil.bridgespero.domain.model.dto.response.ZoomCreateMeetingResponse;
import com.adil.bridgespero.service.ZoomService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/zoom/oauth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ZoomOAuthController {

    ZoomService zoomService;

    @GetMapping("/authorize/{teacherId}")
    public ResponseEntity<Void> authorize(@PathVariable Long teacherId) {
        URI uri = zoomService.buildAuthorizationUri(teacherId);
        return ResponseEntity.status(302).location(uri).build();
    }

    @GetMapping("/callback")
    public ResponseEntity<String> callback(
            @RequestParam("code") String code,
            @RequestParam(value = "state") Long teacherId) {

        zoomService.exchangeCodeForTokens(code, teacherId);

        return ResponseEntity.ok("Zoom connected. Teacher id: " + teacherId);
    }

    @PostMapping("/create-meeting/{teacherId}")
    public ResponseEntity<ZoomCreateMeetingResponse> createMeeting(@PathVariable Long teacherId) {
        return ResponseEntity.ok(zoomService.createInstantMeeting(teacherId));
    }
}