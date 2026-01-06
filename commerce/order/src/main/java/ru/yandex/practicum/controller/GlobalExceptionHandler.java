package ru.yandex.practicum.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.yandex.practicum.exception.ApiError;
import ru.yandex.practicum.exception.order.NoOrderFoundException;

import java.time.LocalDateTime;
import java.util.ConcurrentModificationException;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler({
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<ApiError> handleConstraintViolation(Throwable e, HttpServletRequest request) {
        log.debug("VALIDATION FAILED: {}", e.getMessage());
        ApiError apiError = createApiError(e, HttpStatus.BAD_REQUEST, "Validation failed");
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class
    })
    public ResponseEntity<ApiError> handleIllegalArgument(Throwable e, HttpServletRequest request) {
        log.debug("ILLEGAL ARGUMENT: {}", e.getMessage());
        ApiError apiError = createApiError(e, HttpStatus.BAD_REQUEST, "Invalid request parameters");
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoOrderFoundException.class)
    public ResponseEntity<ApiError> handleNoOrderFoundException(
            NoOrderFoundException e, HttpServletRequest request) {
        log.debug("ORDER NOT FOUND: {}", e.getMessage());
        ApiError apiError = createApiError(e, HttpStatus.NOT_FOUND, "Order not found");
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConcurrentModificationException.class)
    public ResponseEntity<ApiError> handleConcurrentModificationException(
            ConcurrentModificationException e, HttpServletRequest request) {
        log.debug("CONCURRENT MODIFICATION: {}", e.getMessage());
        ApiError apiError = createApiError(e, HttpStatus.CONFLICT, "Resource modified by another process");
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(
            Exception e, HttpServletRequest request) {
        log.error("UNEXPECTED ERROR: {}", e.getMessage(), e);
        ApiError apiError = createApiError(e, HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ApiError createApiError(Throwable e, HttpStatus status, String defaultMessage) {
        String message = e.getMessage() != null ? e.getMessage() : defaultMessage;
        return new ApiError(message, status, LocalDateTime.now());
    }
}