package com.adil.bridgespero.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonConstant {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class HttpAttribute {
        public static final String AUTHORIZATION = "Authorization";
        public static final String BEARER = "Bearer ";
        public static final String SET_COOKIE = "Set-Cookie";
        public static final String ACCESS_TOKEN = "access_token";
    }

}
