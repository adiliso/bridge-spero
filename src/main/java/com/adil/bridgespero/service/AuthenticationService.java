package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.model.dto.TokenPair;
import com.adil.bridgespero.domain.model.dto.request.SigninRequest;
import com.adil.bridgespero.domain.model.dto.request.TeacherSignupRequest;
import com.adil.bridgespero.domain.model.dto.request.UserSignupRequest;
import com.adil.bridgespero.domain.model.dto.response.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {


    public AuthResponse signupUser(UserSignupRequest request) {
    }

    public void signupTeacher(TeacherSignupRequest request) {
    }

    public TokenPair signin(SigninRequest signinRequest) {
    }

    public void signout(String authorizationHeader) {
    }

    public void verify(String authorizationHeader) {
    }

    public TokenPair refreshToken(String refreshToken) {
    }
}
