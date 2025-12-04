package com.adil.bridgespero.exception;

import static com.adil.bridgespero.domain.model.enums.ErrorCode.RESOURCE_NOT_FOUND;

public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(Long id) {
        super("Resource with id " + id + " not found!", RESOURCE_NOT_FOUND);
    }
}
