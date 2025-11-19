package com.adil.bridgespero.exception;

import com.adil.bridgespero.domain.model.dto.response.FieldErrorResponse;
import com.adil.bridgespero.domain.model.dto.response.GlobalErrorResponse;
import com.adil.bridgespero.domain.model.enums.ErrorCode;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.View;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.adil.bridgespero.domain.model.enums.ErrorCode.BAD_REQUEST;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final View error;

    public GlobalExceptionHandler(View error) {
        this.error = error;
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<GlobalErrorResponse> handleBaseException(BaseException ex) {
        addErrorLog(ex.getCode().getHttpStatus(), ex.getMessage(), ex.getClass().getSimpleName());
        return ResponseEntity.status(Optional
                        .ofNullable(ex.getCode().getHttpStatus())
                        .orElse(HttpStatus.INTERNAL_SERVER_ERROR))
                .body(GlobalErrorResponse.builder()
                        .errorCode(ex.getCode())
                        .errorMessage(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .requestId(UUID.randomUUID())
                        .build());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<GlobalErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        addErrorLog(ex.getCode().getHttpStatus(), ex.getMessage(), ex.getClass().getSimpleName());
        return ResponseEntity.status(Optional
                        .ofNullable(ex.getCode().getHttpStatus())
                        .orElse(HttpStatus.INTERNAL_SERVER_ERROR))
                .body(GlobalErrorResponse.builder()
                        .errorCode(ex.getCode())
                        .errorMessage(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .requestId(UUID.randomUUID())
                        .build());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<GlobalErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        addErrorLog(ex.getCode().getHttpStatus(), ex.getMessage(), ex.getClass().getSimpleName());
        return ResponseEntity.status(Optional
                        .ofNullable(ex.getCode().getHttpStatus())
                        .orElse(HttpStatus.INTERNAL_SERVER_ERROR))
                .body(GlobalErrorResponse.builder()
                        .errorCode(ex.getCode())
                        .errorMessage(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .requestId(UUID.randomUUID())
                        .build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<GlobalErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        addErrorLog(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GlobalErrorResponse.builder()
                        .errorCode(BAD_REQUEST)
                        .errorMessage(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .requestId(UUID.randomUUID())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        var response = ex.getFieldErrors()
                .stream()
                .map(error -> new FieldErrorResponse(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        addErrorLog(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GlobalErrorResponse.builder()
                        .errorCode(BAD_REQUEST)
                        .errorMessage("Request contains invalid fields")
                        .errors(response)
                        .timestamp(LocalDateTime.now())
                        .requestId(UUID.randomUUID())
                        .build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GlobalErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {
        addErrorLog(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GlobalErrorResponse.builder()
                        .errorCode(BAD_REQUEST)
                        .errorMessage(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .requestId(UUID.randomUUID())
                        .build());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<GlobalErrorResponse> handleAuthException(AuthenticationException ex) {
        addErrorLog(HttpStatus.UNAUTHORIZED, ex.getMessage(), ex.getClass().getSimpleName());
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(GlobalErrorResponse.builder()
                        .errorCode(ErrorCode.UNAUTHORIZED)
                        .errorMessage(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .requestId(UUID.randomUUID())
                        .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<GlobalErrorResponse> handleAuthException(AccessDeniedException ex) {
        addErrorLog(HttpStatus.UNAUTHORIZED, ex.getMessage(), ex.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(GlobalErrorResponse.builder()
                        .errorCode(ErrorCode.UNAUTHORIZED)
                        .errorMessage(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .requestId(UUID.randomUUID())
                        .build());
    }

    protected void addErrorLog(HttpStatus httpStatus, String errorMessage, String exceptionType) {
        int statusCode = (httpStatus != null) ? httpStatus.value() : HttpStatus.INTERNAL_SERVER_ERROR.value();
        log.error("HTTP Status: {} | Error Message: {} | Exception Type: {}", statusCode, errorMessage, exceptionType);
    }
}