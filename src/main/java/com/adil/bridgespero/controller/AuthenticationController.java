package com.adil.bridgespero.controller;

import com.adil.bridgespero.domain.model.dto.TokenPair;
import com.adil.bridgespero.domain.model.dto.request.RefreshTokenRequest;
import com.adil.bridgespero.domain.model.dto.request.SigninRequest;
import com.adil.bridgespero.domain.model.dto.request.TeacherSignupRequest;
import com.adil.bridgespero.domain.model.dto.request.UserSignupRequest;
import com.adil.bridgespero.service.AuthenticationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping(value = "/signup/user")
    public ResponseEntity<Void> signupUser(@Valid @RequestBody UserSignupRequest request) {
        authenticationService.signupUser(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/signup/teacher", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> signupTeacher(@Valid @ModelAttribute TeacherSignupRequest request) {
        authenticationService.signupTeacher(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signin")
    public ResponseEntity<TokenPair> signin(@Valid @RequestBody SigninRequest signinRequest) {
        return ResponseEntity.ok(authenticationService.signin(signinRequest));
    }

    @GetMapping("/signout")
    public ResponseEntity<Void> signout(@RequestHeader("Authorization") @NotBlank String authorizationHeader) {
        authenticationService.signout(authorizationHeader);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/verify")
    public ResponseEntity<Void> verify(@RequestHeader("Authorization") @NotBlank String authorizationHeader) {
        authenticationService.verify(authorizationHeader);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenPair> refresh(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest.getRefreshToken()));
    }
}
