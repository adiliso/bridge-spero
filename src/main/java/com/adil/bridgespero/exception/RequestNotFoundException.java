package com.adil.bridgespero.exception;

import com.adil.bridgespero.domain.model.enums.ErrorCode;

public class RequestNotFoundException extends BaseException {

    public RequestNotFoundException(Long requestId) {
        super("Request with Id: " + requestId + " not found!", ErrorCode.REQUEST_NOT_FOUND);
    }
}
