package com.nebula.notescape.payload.response;

import com.nebula.notescape.persistence.entity.User;
import lombok.Data;

@Data
public class AuthResponse {
    private final UserResponse user;
    private final String token;

    public AuthResponse(User user, String token) {
        this.user = UserResponse.of(user);
        this.token = token;
    }

}
