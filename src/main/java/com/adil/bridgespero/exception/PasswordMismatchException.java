package com.adil.bridgespero.exception;

import com.adil.bridgespero.domain.model.enums.ErrorCode;

public class PasswordMismatchException extends BaseException {

    public PasswordMismatchException() {
        super("Passwords do not match!", ErrorCode.BAD_REQUEST);
    }
}
