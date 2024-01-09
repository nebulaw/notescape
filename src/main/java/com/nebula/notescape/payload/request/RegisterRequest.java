package com.nebula.notescape.payload.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RegisterRequest {
    private String email;
    private String username;
    private String fullName;
    private String password;
}
