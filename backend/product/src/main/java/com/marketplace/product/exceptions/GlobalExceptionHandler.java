package com.marketplace.product.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.marketplace.product.entities.response.MessageResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> ProductNotFoundExceptionHandler(ProductNotFoundException exc) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(exc.getMessage()));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<?> CategoryNotFoundExceptionHandler(CategoryNotFoundException exc) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(exc.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> ExceptionHandler() {
        return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong"));
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<?> ExceptionHandler(CategoryAlreadyExistsException exc) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse(exc.getMessage()));
    }
}
