package com.nebula.notescape.controller;

import com.nebula.notescape.payload.request.UserRequest;
import com.nebula.notescape.payload.response.ApiResponse;
import com.nebula.notescape.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserController {

    private final IUserService userService;

    @RequestMapping(method = RequestMethod.GET, value = "/find/{username}")
    public ApiResponse getByUsername(@PathVariable String username) {
        return userService.getByUsername(username);
    }

    @GetMapping("/search")
    public ApiResponse get(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "8") Integer size,
            @RequestParam(defaultValue = "username,asc") String[] sort
    ) {
        return userService.get(keyword, page, size, sort);
    }

    @PutMapping("/update")
    public ApiResponse update(
            @RequestHeader("Authorization") String token,
            @RequestBody UserRequest userRequest
    ) {
        return userService.update(token, userRequest);
    }

}