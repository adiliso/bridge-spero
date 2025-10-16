package com.adil.bridgespero.exception;

import com.adil.bridgespero.domain.model.enums.ErrorCode;

public class TeacherNotFoundException extends BaseException {

    public TeacherNotFoundException(Long id) {
        super("Teacher with ID " + id + " not found", ErrorCode.TEACHER_NOT_FOUND);
    }
}
