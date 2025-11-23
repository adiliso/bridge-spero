package com.adil.bridgespero.security;

import com.adil.bridgespero.constant.CommonConstant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TokenUtil {

    public static String extractToken(final String authorizationHeader) {
        TokenValidator.validateAuthorizationHeader(authorizationHeader);
        return authorizationHeader.replace(CommonConstant.HttpAttribute.BEARER, "").trim();
    }

}
