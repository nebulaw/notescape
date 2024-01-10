package com.nebula.notescape.service.impl;

import com.nebula.notescape.exception.IncorrectParameterException;
import com.nebula.notescape.exception.UserAlreadyExistsException;
import com.nebula.notescape.exception.UserNotFoundException;
import com.nebula.notescape.payload.request.UserRequest;
import com.nebula.notescape.payload.response.ApiResponse;
import com.nebula.notescape.persistence.dao.UserDao;
import com.nebula.notescape.persistence.entity.User;
import com.nebula.notescape.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements IUserService {

    private final UserDao userDao;

    @Override
    public ApiResponse getById(Long id) {
        if (id == null || id <= 0) {
            throw new IncorrectParameterException().parameter("id", id);
        } else {
            Optional<User> userOptional = userDao.getById(id);

            if (userOptional.isEmpty()) {
                throw new UserNotFoundException(id);
            } else {
                return ApiResponse.builder()
                        .status(HttpStatus.OK)
                        .data(userOptional.get())
                        .build();
            }
        }
    }

    @Override
    public ApiResponse getByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            throw new IncorrectParameterException().parameter("username", username);
        } else {
            Optional<User> userOptional = userDao.getByUsername(username);

            if (userOptional.isEmpty()) {
                throw new UserNotFoundException(username);
            } else {
                return ApiResponse.builder()
                        .status(HttpStatus.OK)
                        .data(userOptional.get())
                        .build();
            }
        }
    }

    @Override
    public ApiResponse get(String keyword, Integer page, Integer size, String[] sort) {

        return null;
    }

    @Override
    public ApiResponse update(String username, UserRequest request) {
        if (request == null) {
            throw new IncorrectParameterException().parameter("userRequest", "null");
        }

        // Check if user exists
        Optional<User> userOpt = userDao.getByUsername(username);
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException(username);
        }

        // usernames must match
        if (!username.equals(request.getUsername())) {
            throw new IncorrectParameterException()
                    .parameter("username", request.getUsername());
        }

        // If so then update fields
        User user = userOpt.get();
        IncorrectParameterException e = new IncorrectParameterException();

        if (StringUtils.hasText(request.getUsername()) &&
                !request.getUsername().equals(user.getUsername())) {
            // When updating username we must check if it's possible
            if (userDao.existsByUsername(request.getUsername())) {
                throw new UserAlreadyExistsException(request.getUsername());
            }

            if (request.getUsername().length() < 4 ||
                    request.getUsername().length() > 30) {
                e.parameter("username", request.getUsername());
            } else {
                user.setUsername(username);
            }
        }

        if (StringUtils.hasText(request.getEmail()) &&
                !request.getEmail().equals(user.getEmail())) {
            // TODO: validate email
            user.setEmail(request.getEmail());
        }

        if (StringUtils.hasText(request.getFullName()) &&
                !request.getFullName().equals(user.getFullName())) {
            if (request.getFullName().length() < 4) {
                e.parameter("fullName", request.getFullName());
            } else {
                user.setFullName(request.getFullName());
            }
        }

        if (!request.getAbout().equals(user.getAbout())) {
            if (request.getAbout().length() > 240) {
                e.parameter("about", request.getAbout());
            } else {
                user.setAbout(request.getAbout());
            }
        }

        if (StringUtils.hasText(request.getImgUrl()) &&
                !request.getImgUrl().equals(user.getImgUrl())) {
            user.setImgUrl(request.getImgUrl());
        }

        if (!e.getParameters().isEmpty()) {
            throw e;
        } else {
            // After updating fields we save it in the repo
            user = userDao.save(user);

            // And return api response
            return ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .data(user)
                    .build();
        }
    }

    @Override
    public ApiResponse deleteById(Long id) {
        if (id == null || id < 1) {
            throw new IncorrectParameterException()
                    .parameter("id", id);
        } else if (userDao.existsById(id)) {
            throw new UserNotFoundException(id);
        } else {
            userDao.deleteById(id);
            return ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .data("Successfully deleted by id{%d}".formatted(id))
                    .build();
        }
    }

    @Override
    public ApiResponse deleteByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            throw new IncorrectParameterException()
                    .parameter("username", username);
        } else if (userDao.existsByUsername(username)) {
            throw new UserNotFoundException(username);
        } else {
            userDao.deleteByUsername(username);
            return ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .data("'%s' deleted successfully".formatted(username))
                    .build();
        }
    }
}
