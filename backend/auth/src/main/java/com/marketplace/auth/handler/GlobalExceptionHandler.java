package com.marketplace.auth.handler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.marketplace.auth.entity.response.ErrorResponse;
import com.marketplace.auth.exception.CustomJwtException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.persistence.EntityNotFoundException;

@Deprecated
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomJwtException.class)
  public ResponseEntity<?> jwtException(CustomJwtException exc) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exc.getMessage());
  }

  @ExceptionHandler(UnsupportedJwtException.class)
  public ResponseEntity<?> jwtException(UnsupportedJwtException exc) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("JWT token is unsupported:" + exc.getMessage());
  }

  @ExceptionHandler(ExpiredJwtException.class)
  public ResponseEntity<?> jwtException(ExpiredJwtException exc) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("JWT token is expired: " + exc.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<?> jwtException(IllegalArgumentException exc) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("JWT claims string is empty:" + exc.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> jwtException(Exception exc) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Exception: " + exc.getMessage());
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException exception) {
    return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), null);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
    List<String> errors = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .toList();
    return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed", errors);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
    return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", List.of(ex.getMessage()));
  }

  private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, List<String> details) {
    ErrorResponse response = new ErrorResponse(
        LocalDateTime.now(),
        status.value(),
        status.getReasonPhrase(),
        message,
        details);
    return new ResponseEntity<>(response, status);
  }
}
