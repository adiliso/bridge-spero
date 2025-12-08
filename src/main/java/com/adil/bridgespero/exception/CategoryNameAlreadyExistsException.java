package com.adil.bridgespero.exception;

import com.adil.bridgespero.domain.model.enums.ErrorCode;

public class CategoryNameAlreadyExistsException extends BaseException {

    public CategoryNameAlreadyExistsException(String name) {
        super("Category name: " + name + " already exists!", ErrorCode.ALREADY_EXISTS);
    }
}
