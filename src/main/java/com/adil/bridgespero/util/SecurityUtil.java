package com.adil.bridgespero.util;

import com.adil.bridgespero.constant.CommonConstant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityUtil {

    public static String resolveToken(HttpServletRequest request) {
        String authHeader = request.getHeader(CommonConstant.HttpAttribute.AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(CommonConstant.HttpAttribute.BEARER))
            return authHeader.substring(CommonConstant.HttpAttribute.BEARER.length());
        return null;
    }

}
