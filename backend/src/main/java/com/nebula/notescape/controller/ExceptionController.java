package com.nebula.notescape.controller;

import com.nebula.notescape.exception.ApiException;
import com.nebula.notescape.payload.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionController {

  @ResponseBody
  @ExceptionHandler(ApiException.class)
  public ApiResponse handleApiException(ApiException e) {
    return ApiResponse.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .error(e.getMessage())
        .build();
  }

  @ResponseBody
  @ExceptionHandler(Exception.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public ApiResponse handleException(Exception e) {
    return ApiResponse.builder()
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .error(e.getMessage())
        .build();
  }

}
