package com.nebula.notescape.exception;

import org.springframework.security.core.userdetails.UserDetails;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("User not found by id{%d}".formatted(id));
    }

    public UserNotFoundException(String username) {
        super("'%s' not found".formatted(username));
    }

    public UserNotFoundException(UserDetails userDetails) {
        super("%s not found".formatted(userDetails.getUsername()));
    }

}
