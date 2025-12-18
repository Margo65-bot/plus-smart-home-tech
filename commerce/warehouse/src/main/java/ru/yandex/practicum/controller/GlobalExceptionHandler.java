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
import ru.yandex.practicum.exception.warehouse.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exception.warehouse.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.exception.warehouse.SpecifiedProductAlreadyInWarehouseException;

import java.time.LocalDateTime;

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

        String message = e.getMessage() != null ? e.getMessage() : "Invalid request";
        ApiError apiError = new ApiError(message, HttpStatus.BAD_REQUEST, LocalDateTime.now());

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSpecifiedProductInWarehouseException.class)
    public ResponseEntity<ApiError> handleNoSpecifiedProductInWarehouseException(
            NoSpecifiedProductInWarehouseException e, HttpServletRequest request) {
        log.debug("NOT FOUND: {}", e.getMessage());

        String message = e.getMessage() != null ? e.getMessage() : "Product not found in warehouse";
        ApiError apiError = new ApiError(message, HttpStatus.NOT_FOUND, LocalDateTime.now());

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SpecifiedProductAlreadyInWarehouseException.class)
    public ResponseEntity<ApiError> handleSpecifiedProductAlreadyInWarehouseException(
            SpecifiedProductAlreadyInWarehouseException e, HttpServletRequest request) {
        log.debug("ALREADY EXISTS: {}", e.getMessage());

        String message = e.getMessage() != null ? e.getMessage() : "Product already exists in warehouse";
        ApiError apiError = new ApiError(message, HttpStatus.CONFLICT, LocalDateTime.now());

        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ProductInShoppingCartLowQuantityInWarehouse.class)
    public ResponseEntity<ApiError> handleProductInShoppingCartLowQuantityInWarehouseException(
            ProductInShoppingCartLowQuantityInWarehouse e, HttpServletRequest request) {
        log.debug("LOW QUANTITY: {}", e.getMessage());

        String message = e.getMessage() != null ? e.getMessage() : "Insufficient product quantity in warehouse";
        ApiError apiError = new ApiError(message, HttpStatus.UNPROCESSABLE_ENTITY, LocalDateTime.now());

        return new ResponseEntity<>(apiError, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private String extractValidationMessage(Exception e) {
        final StringBuilder messageBuilder = new StringBuilder("Validation failed");

        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            if (!ex.getBindingResult().getFieldErrors().isEmpty()) {
                messageBuilder.setLength(0);
                ex.getBindingResult().getFieldErrors().forEach(error ->
                        messageBuilder.append(String.format("%s: %s; ", error.getField(), error.getDefaultMessage()))
                );
            }
        }

        String result = messageBuilder.toString();
        if (result.endsWith("; ")) {
            result = result.substring(0, result.length() - 2);
        }
        return result;
    }
}
