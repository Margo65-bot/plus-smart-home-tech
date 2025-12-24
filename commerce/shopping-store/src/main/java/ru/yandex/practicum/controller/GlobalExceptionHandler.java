package ru.yandex.practicum.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
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
import ru.yandex.practicum.exception.shopping_store.ProductNotFoundException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<ApiError> handleConstraintViolation(Exception e, HttpServletRequest request) {
        log.debug("VALIDATION FAILED: {}", e.getMessage());

        String message = extractValidationMessage(e);
        ApiError apiError = new ApiError(message, HttpStatus.BAD_REQUEST, LocalDateTime.now());

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class
    })
    public ResponseEntity<ApiError> handleIllegalArgument(Exception e, HttpServletRequest request) {
        log.debug("ILLEGAL ARGUMENT: {}", e.getMessage());

        String message = e.getMessage() != null ? e.getMessage() : "Invalid request parameters";
        ApiError apiError = new ApiError(message, HttpStatus.BAD_REQUEST, LocalDateTime.now());

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(
            ProductNotFoundException e, HttpServletRequest request) {
        log.debug("NOT FOUND: {}", e.getMessage());

        String message = e.getMessage() != null ? e.getMessage() : "Resource not found";
        ApiError apiError = new ApiError(message, HttpStatus.NOT_FOUND, LocalDateTime.now());

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(
            Exception e, HttpServletRequest request) {
        log.error("UNEXPECTED ERROR: {}", e.getMessage(), e);

        ApiError apiError = new ApiError(
                "Internal server error",
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String extractValidationMessage(Exception e) {
        if (e instanceof MethodArgumentNotValidException) {
            return ((MethodArgumentNotValidException) e).getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                    .collect(Collectors.joining("; "));
        } else if (e instanceof ConstraintViolationException) {
            return ((ConstraintViolationException) e).getConstraintViolations()
                    .stream()
                    .map(violation -> String.format("%s: %s",
                            violation.getPropertyPath(),
                            violation.getMessage()))
                    .collect(Collectors.joining("; "));
        }
        return e.getMessage() != null ? e.getMessage() : "Validation failed";
    }
}
