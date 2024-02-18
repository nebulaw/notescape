package com.nebula.notescape.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

  private String message;

  public ApiException(String message) {
    super(message);
  }

}
