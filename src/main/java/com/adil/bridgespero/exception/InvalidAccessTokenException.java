package com.adil.bridgespero.exception;

public class InvalidAccessTokenException extends AuthException {

    public InvalidAccessTokenException() {
        super("Invalid access token!");
    }

}
