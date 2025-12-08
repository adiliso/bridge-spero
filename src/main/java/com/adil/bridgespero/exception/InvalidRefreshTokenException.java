package com.adil.bridgespero.exception;

public class InvalidRefreshTokenException extends AuthException {

    public InvalidRefreshTokenException() {
        super("Invalid refresh token!");
    }

}
