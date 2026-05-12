package com.phananh.e_commerce.application.exception;

import com.phananh.e_commerce.presentation.dto.response.ApiResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<?>> handleAppException(AppException ex, WebRequest request) {
        log.error("AppException occurred: ", ex);
        ErrorCode errorCode = ex.getErrorCode();

        ApiResponse<?> response = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(response, errorCode.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest request) {
        log.error("Validation error occurred: ", ex);

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiResponse<?> response = ApiResponse.builder()
                .code(ErrorCode.VALIDATION_ERROR.getCode())
                .message("Validation failed")
                .result(errors)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<?>> handleResponseStatusException(
            ResponseStatusException ex, WebRequest request) {
        log.error("ResponseStatusException occurred: ", ex);

        ErrorCode errorCode = mapHttpStatusToErrorCode(ex.getStatusCode());
        String message = ex.getReason() != null ? ex.getReason() : errorCode.getMessage();

        ApiResponse<?> response = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(message)
                .build();

        return new ResponseEntity<>(response, ex.getStatusCode());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {
        log.error("Access denied: ", ex);

        ApiResponse<?> response = ApiResponse.builder()
                .code(ErrorCode.FORBIDDEN.getCode())
                .message(ErrorCode.FORBIDDEN.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGlobalException(Exception ex, WebRequest request) {
        log.error("Unexpected error occurred: ", ex);

        ApiResponse<?> response = ApiResponse.builder()
                .code(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
                .message(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorCode mapHttpStatusToErrorCode(Object statusCode) {
        if (statusCode instanceof HttpStatus status) {
            return switch (status) {
                case BAD_REQUEST -> ErrorCode.INVALID_REQUEST;
                case UNAUTHORIZED -> ErrorCode.UNAUTHORIZED;
                case FORBIDDEN -> ErrorCode.FORBIDDEN;
                case NOT_FOUND -> ErrorCode.NOT_FOUND;
                case CONFLICT -> ErrorCode.CONFLICT;
                default -> ErrorCode.INTERNAL_SERVER_ERROR;
            };
        }
        return ErrorCode.INTERNAL_SERVER_ERROR;
    }
}

