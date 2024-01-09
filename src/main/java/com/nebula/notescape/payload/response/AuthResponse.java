package com.nebula.notescape.payload.response;

public class AuthResponse {

    private AuthResponse() {
    }

    public static AuthResponseBuilder builder() {
        return new AuthResponseBuilder();
    }

    public static class AuthResponseBuilder extends ApiResponse.ApiResponseBuilder {

        public AuthResponseBuilder token(String token) {
            super.put("token", token);
            return this;
        }

        public AuthResponseBuilder user(UserResponse userResponse) {
            super.put("user", userResponse);
            return this;
        }

    }

}
