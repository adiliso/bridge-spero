package com.adil.bridgespero.exception;

import static com.adil.bridgespero.domain.model.enums.ErrorCode.CATEGORY_NOT_FOUND;

public class CategoryNotFoundException extends BaseException {

    public CategoryNotFoundException(Long id) {
        super("Category with ID " + id + " not found", CATEGORY_NOT_FOUND);
    }
}
