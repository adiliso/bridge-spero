package com.adil.bridgespero.exception;

import static com.adil.bridgespero.domain.model.enums.ErrorCode.TEACHER_NOT_FOUND;

public class TeacherNotFoundException extends BaseException {

    public TeacherNotFoundException(Long id) {
        super("Teacher with ID " + id + " not found", TEACHER_NOT_FOUND);
    }
}
