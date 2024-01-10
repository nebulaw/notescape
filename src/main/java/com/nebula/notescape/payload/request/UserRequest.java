package com.nebula.notescape.payload.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserRequest {
    private String username;
    private String email;
    private String fullName;
    private String about;
    private String imgUrl;
    private String password;
}
