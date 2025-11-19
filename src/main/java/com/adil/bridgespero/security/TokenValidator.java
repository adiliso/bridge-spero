package com.adil.bridgespero.security;

import com.adil.bridgespero.constant.CommonConstant;
import com.adil.bridgespero.exception.InvalidAccessTokenException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TokenValidator {

    public static void validateAuthorizationHeader(String authorizationHeader) {
        if (StringUtils.isBlank(authorizationHeader) || !authorizationHeader.startsWith(CommonConstant.HttpAttribute.BEARER) ||
            StringUtils.isBlank(authorizationHeader.replace(CommonConstant.HttpAttribute.BEARER, "")))
            throw new InvalidAccessTokenException();
    }

}
