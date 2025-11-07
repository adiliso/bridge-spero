package com.adil.bridgespero.exception;

import static com.adil.bridgespero.domain.model.enums.ErrorCode.SYLLABUS_ALREADY_EXISTS;

public class SyllabusAlreadyExistsException extends BaseException {

    public SyllabusAlreadyExistsException(Long groupId) {
        super("Group with ID " + groupId + "has syllabus", SYLLABUS_ALREADY_EXISTS);
    }
}
