package com.marketplace.auth.exception;

public class CustomJwtException extends RuntimeException {
  public CustomJwtException() {
    super();
  }

  public CustomJwtException(String text) {
    super(text);
  }
}
