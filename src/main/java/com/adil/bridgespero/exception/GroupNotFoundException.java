package com.adil.bridgespero.exception;

import static com.adil.bridgespero.domain.model.enums.ErrorCode.GROUP_NOT_FOUND;

public class GroupNotFoundException extends BaseException {

    public GroupNotFoundException(Long id) {
        super("Group with ID " + id + " not found", GROUP_NOT_FOUND);
    }
}
