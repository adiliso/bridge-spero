package com.adil.bridgespero.exception;

import com.adil.bridgespero.domain.model.enums.ErrorCode;

public class UserNotEnabledException extends BaseException {

    public UserNotEnabledException() {
        super("User is not enabled", ErrorCode.UNAUTHORIZED);
    }
}
