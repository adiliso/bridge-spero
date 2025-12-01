package com.adil.bridgespero.service;

import com.adil.bridgespero.common.TokenProvider;
import com.adil.bridgespero.domain.model.dto.TeacherDto;
import com.adil.bridgespero.domain.model.dto.TokenPair;
import com.adil.bridgespero.domain.model.dto.UserDto;
import com.adil.bridgespero.domain.model.dto.request.SigninRequest;
import com.adil.bridgespero.domain.model.dto.request.TeacherSignupRequest;
import com.adil.bridgespero.domain.model.dto.request.UserSignupRequest;
import com.adil.bridgespero.domain.model.enums.ResourceType;
import com.adil.bridgespero.domain.model.enums.Role;
import com.adil.bridgespero.domain.repository.TokenRedisRepository;
import com.adil.bridgespero.exception.InvalidAccessTokenException;
import com.adil.bridgespero.exception.InvalidRefreshTokenException;
import com.adil.bridgespero.exception.PasswordMismatchException;
import com.adil.bridgespero.security.TokenUtil;
import com.adil.bridgespero.security.jwt.TokenCreator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    UserService userService;
    TeacherService teacherService;
    FileStorageService fileStorageService;
    TokenCreator tokenCreator;
    TokenProvider tokenProvider;
    PasswordEncoder passwordEncoder;
    TokenRedisRepository tokenRedisRepository;
    AuthenticationManager authenticationManager;

    public void signupUser(UserSignupRequest signupRequest) {
        checkPasswordsMatch(signupRequest.getPassword(), signupRequest.getConfirmPassword());
        userService.checkEmailAlreadyExists(signupRequest.getEmail());
        UserDto userDto = UserDto.builder()
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .name(signupRequest.getName())
                .surname(signupRequest.getSurname())
                .role(Role.USER)
                .phone(signupRequest.getPhoneCode() + signupRequest.getPhoneNumber())
                .enabled(true)
                .agreedToTerms(signupRequest.isAgreedToTerms())
                .build();
        userService.save(userDto);
    }

    public void signupTeacher(TeacherSignupRequest signupRequest) {
        checkPasswordsMatch(signupRequest.getPassword(), signupRequest.getConfirmPassword());
        userService.checkEmailAlreadyExists(signupRequest.getEmail());
        TeacherDto teacherDto = TeacherDto.builder()
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .name(signupRequest.getName())
                .surname(signupRequest.getSurname())
                .role(Role.USER)
                .phone(signupRequest.getPhoneCode() + signupRequest.getPhoneNumber())
                .subjects(signupRequest.getSubjects())
                .enabled(true)
                .agreedToTerms(signupRequest.isAgreedToTerms())
                .bio(signupRequest.getBio())
                .experience(signupRequest.getExperience())
                .demoVideoUrl(fileStorageService.saveFile(signupRequest.getDemoVideo(), ResourceType.DEMO_VIDEO))
                .build();
        teacherService.save(teacherDto);
    }

    private void checkPasswordsMatch(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) throw new PasswordMismatchException();
    }

    public TokenPair signin(SigninRequest signinRequest) {
        var authToken = new UsernamePasswordAuthenticationToken(
                signinRequest.getEmail(),
                signinRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(authToken);
        return createAndSaveToken(authentication);
    }

    public void signout(String authHeader) {
        final String accessToken = TokenUtil.extractToken(authHeader);
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final TokenPair currentTokenPair = tokenRedisRepository.read(authentication.getName());

        if (currentTokenPair == null || !Objects.equals(accessToken, currentTokenPair.getAccessToken()))
            throw new InvalidAccessTokenException();

        tokenRedisRepository.delete(authentication.getName());
    }

    public void verify(String authHeader) {
        final String accessToken = TokenUtil.extractToken(authHeader);
        final Authentication authentication = tokenProvider.parseAuthentication(accessToken);
        final TokenPair tokenPair = tokenRedisRepository.read(authentication.getName());

        Optional.ofNullable(tokenPair)
                .map(TokenPair::getAccessToken)
                .filter(accessToken::equals)
                .orElseThrow(InvalidAccessTokenException::new);
    }

    public TokenPair refreshToken(String refreshToken) {
        tokenProvider.validateToken(refreshToken, InvalidRefreshTokenException::new);
        final Authentication authentication = tokenProvider.parseAuthentication(refreshToken);
        final TokenPair currentTokenPair = tokenRedisRepository.read(authentication.getName());

        if (currentTokenPair == null || !Objects.equals(refreshToken, currentTokenPair.getRefreshToken()))
            throw new InvalidRefreshTokenException();

        final TokenPair newTokenPair = tokenCreator.createTokenPair(authentication);
        tokenRedisRepository.update(authentication.getName(), newTokenPair);
        return newTokenPair;
    }

    private TokenPair createAndSaveToken(Authentication authentication) {
        final TokenPair tokenPair = tokenCreator.createTokenPair(authentication);
        tokenRedisRepository.save(authentication.getName(), tokenPair);
        return tokenPair;
    }
}
