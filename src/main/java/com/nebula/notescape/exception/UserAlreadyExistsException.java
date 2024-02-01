package com.nebula.notescape.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String username) {
        super("%s already exists".formatted(username));
    }

}
