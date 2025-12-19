package com.adil.bridgespero.exception;

import com.adil.bridgespero.domain.model.enums.ErrorCode;

public class RequestDuplicationException extends BaseException {

    public RequestDuplicationException(Long studentId, Long groupId) {
        super("Student " + studentId + " already has pending request for group " + groupId, ErrorCode.ALREADY_EXISTS);
    }
}
