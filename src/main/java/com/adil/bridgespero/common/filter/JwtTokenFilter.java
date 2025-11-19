package com.adil.bridgespero.common.filter;

import com.adil.bridgespero.common.TokenProvider;
import com.adil.bridgespero.util.SecurityUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        log.debug("JwtTokenFilter is calling for uri: {} {}",
                httpServletRequest.getMethod(), httpServletRequest.getRequestURI());

        final String jwt = SecurityUtil.resolveToken(httpServletRequest);
        final String requestUri = httpServletRequest.getRequestURI();

        if (StringUtils.hasText(jwt) && tokenProvider.isValidToken(jwt)) {
            Authentication authentication = tokenProvider.parseAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Set Authentication to security context for '{}', uri: {}",
                    authentication.getName(), requestUri);
        }

        filterChain.doFilter(request, response);
    }

}
