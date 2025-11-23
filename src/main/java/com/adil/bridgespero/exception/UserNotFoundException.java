package com.adil.bridgespero.exception;

import static com.adil.bridgespero.domain.model.enums.ErrorCode.USER_NOT_FOUND;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException(Long id) {
        super("User with ID " + id + " not found", USER_NOT_FOUND);
    }

    public UserNotFoundException(String email) {
        super("User with email " + email + " not found", USER_NOT_FOUND);
    }
}
