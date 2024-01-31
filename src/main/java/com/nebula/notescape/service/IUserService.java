package com.nebula.notescape.service;

import com.nebula.notescape.payload.request.UserRequest;
import com.nebula.notescape.payload.response.ApiResponse;

public interface IUserService {

    ApiResponse getById(Long id);

    ApiResponse getByUsername(String username);

    ApiResponse getUsersByKeyword(String keyword, Integer page, Integer size, String[] sort);

    ApiResponse update(String token, UserRequest userRequest);

    ApiResponse deleteById(Long id);

    ApiResponse deleteByUsername(String username);

}
