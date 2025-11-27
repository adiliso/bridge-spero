package com.adil.bridgespero.exception;

import com.adil.bridgespero.domain.model.enums.ErrorCode;

public class StateNotFoundException extends BaseException {

    public StateNotFoundException() {
        super("State not found!", ErrorCode.STATE_NOT_FOUND);
    }
}
