package com.nebula.notescape.exception;

import org.springframework.security.core.userdetails.UserDetails;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(UserDetails userDetails) {
        super("%s not found".formatted(userDetails.getUsername()));
    }

}
