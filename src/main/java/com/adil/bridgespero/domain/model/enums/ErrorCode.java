package com.adil.bridgespero.domain.model.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND),
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND),
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND),
    STATE_NOT_FOUND(HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND),

    TEACHER_ALREADY_EXISTS(HttpStatus.CONFLICT),
    SYLLABUS_ALREADY_EXISTS(HttpStatus.CONFLICT),

    BAD_REQUEST(HttpStatus.BAD_REQUEST),
    ALREADY_EXISTS(HttpStatus.BAD_REQUEST),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED),

    FORBIDDEN(HttpStatus.FORBIDDEN),

    USER_NOT_CONNECTED_TO_ZOOM(HttpStatus.UNAUTHORIZED),
    ZOOM_TOKEN_REFRESH_FAILED(HttpStatus.UNAUTHORIZED),
    ZOOM_OAUTH_TOKEN_EXCHANGE_FAILED(HttpStatus.BAD_REQUEST),
    ZOOM_API_CALL_FAILED(HttpStatus.BAD_GATEWAY),
    ZOOM_CALLBACK_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),

    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

    private final HttpStatus httpStatus;

    ErrorCode(HttpStatus status) {
        this.httpStatus = status;
    }
}

