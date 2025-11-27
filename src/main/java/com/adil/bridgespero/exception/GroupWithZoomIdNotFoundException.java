package com.adil.bridgespero.exception;

import static com.adil.bridgespero.domain.model.enums.ErrorCode.GROUP_NOT_FOUND;

public class GroupWithZoomIdNotFoundException extends BaseException {

    public GroupWithZoomIdNotFoundException(Long id) {
        super("Group with Zoom ID " + id + " not found", GROUP_NOT_FOUND);
    }
}
