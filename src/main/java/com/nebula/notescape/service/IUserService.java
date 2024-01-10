package com.nebula.notescape.service;

import com.nebula.notescape.payload.request.UserRequest;
import com.nebula.notescape.payload.response.ApiResponse;

public interface IUserService {

    ApiResponse getById(Long id);

    ApiResponse getByUsername(String username);

    ApiResponse get(String keyword, Integer page, Integer size, String[] sort);

    ApiResponse update(String username, UserRequest userRequest);

    ApiResponse deleteByUsername(String username);

}
