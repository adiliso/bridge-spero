package com.adil.bridgespero.exception;

import com.adil.bridgespero.domain.model.enums.ErrorCode;

public class InvalidRefreshTokenException extends AuthException {

    public InvalidRefreshTokenException() {
        super("Invalid refresh token!", ErrorCode.UNAUTHORIZED);
    }

}
