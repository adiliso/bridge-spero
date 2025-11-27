package com.adil.bridgespero.security.jwt;

import com.adil.bridgespero.common.TokenKey;
import com.adil.bridgespero.config.properties.TokenProperties;
import com.adil.bridgespero.domain.model.dto.TokenPair;
import com.adil.bridgespero.domain.model.enums.TokenType;
import com.adil.bridgespero.security.model.CustomUserPrincipal;
import com.adil.bridgespero.util.FormatterUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenCreator {

    private final TokenProperties tokenProperties;
    private Key key;

    @PostConstruct
    private void init() {
        byte[] keyBytes = Decoders.BASE64.decode(tokenProperties.getBase64Secret());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenPair createTokenPair(Authentication authentication) {
        return TokenPair.builder()
                .accessToken(createToken(authentication, TokenType.ACCESS))
                .refreshToken(createToken(authentication, TokenType.REFRESH)).build();
    }

    private String createToken(Authentication authentication, TokenType tokenType) {
        final String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        final CustomUserPrincipal customUserPrincipal = (CustomUserPrincipal) authentication.getPrincipal();

        final Long tokenValidityInSeconds = (tokenType == TokenType.ACCESS) ?
                tokenProperties.getAccessTokenValidityInSeconds() : tokenProperties.getRefreshTokenValidityInSeconds();
        final LocalDateTime validity = LocalDateTime.now().plusSeconds(tokenValidityInSeconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(TokenKey.ID, customUserPrincipal.getId())
                .claim(TokenKey.TOKEN_TYPE, tokenType)
                .claim(TokenKey.AUTHORITIES, authorities)
                .claim(TokenKey.FULL_NAME, customUserPrincipal.getFullName())
                .claim(TokenKey.ROLE, customUserPrincipal.getRole())
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(FormatterUtil.convertToUtilDate(validity))
                .compact();
    }
}
