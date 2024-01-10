package com.nebula.notescape.controller;

import com.nebula.notescape.exception.IncorrectParameterException;
import com.nebula.notescape.exception.UserAlreadyExistsException;
import com.nebula.notescape.exception.UserNotFoundException;
import com.nebula.notescape.payload.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionController {

    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ApiResponse handleUserNotFound(UserNotFoundException e) {
        return ApiResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .error(e.getMessage())
                .build();
    }

    @ResponseBody
    @ExceptionHandler(IncorrectParameterException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiResponse handleIncorrectParameter(IncorrectParameterException e) {
        return ApiResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .data(e.getParameters())
                .error(e.getMessage())
                .build();
    }

    @ResponseBody
    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ApiResponse handleUsernameExists(UserAlreadyExistsException e) {
        return ApiResponse.builder()
                .status(HttpStatus.CONFLICT)
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
