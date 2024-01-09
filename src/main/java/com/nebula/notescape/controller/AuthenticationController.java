package com.nebula.notescape.controller;

import com.nebula.notescape.payload.request.LoginRequest;
import com.nebula.notescape.payload.request.RegisterRequest;
import com.nebula.notescape.payload.response.ApiResponse;
import com.nebula.notescape.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ApiResponse login(@RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ApiResponse register(@RequestBody RegisterRequest registerRequest) {
        return authenticationService.register(registerRequest);
    }

}
