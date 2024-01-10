package com.nebula.notescape.controller;

import com.nebula.notescape.payload.response.ApiResponse;
import com.nebula.notescape.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final IUserService userService;

    @RequestMapping(method = RequestMethod.GET, value = "/find/{username}")
    public ApiResponse getByUsername(@PathVariable String username) {
        return userService.getByUsername(username);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ApiResponse get(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "sort", required = false) String[] sort
    ) {
        return userService.get(keyword, 1, 2, sort);
    }

}
