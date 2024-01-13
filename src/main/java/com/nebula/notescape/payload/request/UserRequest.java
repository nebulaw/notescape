package com.nebula.notescape.payload.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserRequest {
    private String username;
    // user can't update email, it is given for validation between token and one
    private String email;
    private String fullName;
    private String about;
    private String imgUrl;
    private String password;
}
