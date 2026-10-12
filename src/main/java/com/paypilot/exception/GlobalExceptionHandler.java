package com.paypilot.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    GlobalExceptionHandler.class
            );

    // =====================================================
    // BILL NOT FOUND
    // =====================================================

    @ExceptionHandler(
            BillNotFoundException.class
    )
    public ResponseEntity<Map<String, String>>
    handleBillNotFound(
            BillNotFoundException exception) {

        logger.info(
                "Bill not found: {}",
                exception.getMessage()
        );

        Map<String, String> response =
                new HashMap<>();

        response.put(
                "error",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    // =====================================================
    // CATEGORY NOT FOUND
    // =====================================================

    @ExceptionHandler(
            BillCategoryNotFoundException.class
    )
    public ResponseEntity<Map<String, String>>
    handleCategoryNotFound(
            BillCategoryNotFoundException exception) {

        logger.info(
                "Category not found: {}",
                exception.getMessage()
        );

        Map<String, String> response =
                new HashMap<>();

        response.put(
                "error",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    // =====================================================
    // VALIDATION ERROR
    // =====================================================

    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<Map<String, String>>
    handleValidationError(
            MethodArgumentNotValidException exception) {

        logger.info(
                "Validation error occurred"
        );

        Map<String, String> response =
                new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error -> {

                    logger.info(
                            "Validation error - field: {}, message: {}",
                            error.getField(),
                            error.getDefaultMessage()
                    );

                    response.put(
                            error.getField(),
                            error.getDefaultMessage()
                    );
                });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    // =====================================================
    // GENERAL ERROR
    // =====================================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>>
    handleGeneralException(
            Exception exception) {

        logger.info(
                "Unexpected error occurred: {}",
                exception.getMessage()
        );

        Map<String, String> response =
                new HashMap<>();

        response.put(
                "error",
                "Internal server error"
        );

        return ResponseEntity
                .status(
                        HttpStatus.INTERNAL_SERVER_ERROR
                )
                .body(response);
    }
}
