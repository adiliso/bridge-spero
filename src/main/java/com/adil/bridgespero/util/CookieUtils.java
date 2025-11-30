package com.adil.bridgespero.util;

import org.springframework.http.ResponseCookie;

public class CookieUtils {

    public static String createHttpOnlyCookie(String name, String value) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)            // Essential: Prevents JavaScript access (XSS protection)
                .secure(true)              // Essential: Only transmit over HTTPS
                .path("/")                 // Sets the cookie's expiration time
                .build()
                .toString();
    }
}