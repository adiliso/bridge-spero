package com.adil.bridgespero.exception;

import com.adil.bridgespero.domain.model.enums.ErrorCode;

public class AuthException extends BaseException {

    public AuthException(String message, ErrorCode code) {
        super(message, code);
    }
}
