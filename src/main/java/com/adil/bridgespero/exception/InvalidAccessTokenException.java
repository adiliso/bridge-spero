package com.adil.bridgespero.exception;

import com.adil.bridgespero.domain.model.enums.ErrorCode;

public class InvalidAccessTokenException extends AuthException {

    public InvalidAccessTokenException() {
        super("Invalid access token!", ErrorCode.UNAUTHORIZED);
    }

}
