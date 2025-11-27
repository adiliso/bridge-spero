package com.adil.bridgespero.common;

import com.adil.bridgespero.domain.model.enums.ErrorCode;
import com.adil.bridgespero.domain.model.enums.Role;
import com.adil.bridgespero.exception.AuthException;
import com.adil.bridgespero.security.model.CustomUserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private Key key;
    private static final List<GrantedAuthority> AUTHORITIES = List.of(new SimpleGrantedAuthority("USER"));

    @Value("${application.security.authentication.jwt.base64-secret:}")
    private String base64Secret;


    @PostConstruct
    public void init() {
        log.info("Debug Secret Length: {}", (base64Secret != null) ? base64Secret.length() : "NULL");
        byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public <T extends AuthException> void validateToken(String token, Supplier<T> exceptionSupplier) {
        if (!isValidToken(token)) throw exceptionSupplier.get();
    }

    public boolean isValidToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("Invalid JWT signature");
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            log.warn("JWT token compact of handler are invalid");
        } catch (JwtException e) {
            log.warn("Invalid JWT token");
        }
        return false;
    }

    public Authentication parseAuthentication(String authToken) {
        validateToken(authToken, () -> new AuthException("Invalid token!", ErrorCode.UNAUTHORIZED));
        final Claims claims = extractClaim(authToken);
        final User principal = getPrincipal(claims, AUTHORITIES);
        return new UsernamePasswordAuthenticationToken(principal, authToken, AUTHORITIES);
    }

    private Claims extractClaim(String authToken) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(authToken)
                .getBody();
    }

    private CustomUserPrincipal getPrincipal(Claims claims, Collection<? extends GrantedAuthority> authorities) {
        Long id = claims.get(TokenKey.ID, Long.class);
        String subject = claims.getSubject();
        String tokenType = claims.get(TokenKey.TOKEN_TYPE, String.class);
        String fullName = claims.get(TokenKey.FULL_NAME, String.class);
        Role role = Role.valueOf(claims.get(TokenKey.ROLE, String.class));
        return new CustomUserPrincipal(id, subject, fullName, tokenType, "USER", role, authorities);
    }

}