package com.marketplace.auth.exception;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String name) {
    super(new String("Username with name " + '\'' + name + "\' not found"));
  }
}
