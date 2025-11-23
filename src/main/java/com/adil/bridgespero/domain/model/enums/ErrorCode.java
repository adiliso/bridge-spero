package com.adil.bridgespero.domain.model.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND),
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND),
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND),
    TEACHER_ALREADY_EXISTS(HttpStatus.CONFLICT),
    SYLLABUS_ALREADY_EXISTS(HttpStatus.CONFLICT),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(HttpStatus.BAD_REQUEST),
    ALREADY_EXISTS(HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED),
    FORBIDDEN(HttpStatus.FORBIDDEN);

    private final HttpStatus httpStatus;

    ErrorCode(HttpStatus status) {
        this.httpStatus = status;
    }
}

