package com.adil.bridgespero.exception;

import com.adil.bridgespero.domain.model.enums.ErrorCode;

public class EmailAlreadyExistsException extends BaseException {

    public EmailAlreadyExistsException() {
        super("Email already exists!", ErrorCode.ALREADY_EXISTS);
    }
}
