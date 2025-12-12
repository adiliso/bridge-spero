package com.adil.bridgespero.exception;

import com.adil.bridgespero.domain.model.enums.ErrorCode;

public class UserGroupAlreadyExistsException extends BaseException {

    public UserGroupAlreadyExistsException(Long userId, Long groupId) {
        super(String.format("User: %d is already member of group: %d", userId, groupId), ErrorCode.ALREADY_EXISTS);
    }
}
