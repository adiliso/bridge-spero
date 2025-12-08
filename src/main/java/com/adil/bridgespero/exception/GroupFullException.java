package com.adil.bridgespero.exception;

import com.adil.bridgespero.domain.model.enums.ErrorCode;

public class GroupFullException extends BaseException {

    public GroupFullException(Long id) {
        super("Group with ID: " + id + " is full!", ErrorCode.GROUP_IS_FULL);
    }
}
