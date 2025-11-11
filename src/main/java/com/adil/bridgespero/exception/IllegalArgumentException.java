package com.adil.bridgespero.exception;

import com.adil.bridgespero.domain.model.enums.ErrorCode;

public class IllegalArgumentException extends BaseException {

    public IllegalArgumentException(String message) {
        super(message, ErrorCode.BAD_REQUEST);
    }
}
