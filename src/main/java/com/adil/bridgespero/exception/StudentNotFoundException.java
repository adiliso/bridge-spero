package com.adil.bridgespero.exception;

import static com.adil.bridgespero.domain.model.enums.ErrorCode.STUDENT_NOT_FOUND;

public class StudentNotFoundException extends BaseException {
    public StudentNotFoundException(Long id) {
        super("Student with ID " + id + " not found", STUDENT_NOT_FOUND);
    }

}
