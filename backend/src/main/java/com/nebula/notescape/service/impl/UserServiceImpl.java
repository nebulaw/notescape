package com.nebula.notescape.service.impl;

import com.nebula.notescape.exception.ApiException;
import com.nebula.notescape.exception.Exceptions;
import com.nebula.notescape.payload.request.UserRequest;
import com.nebula.notescape.payload.response.ApiResponse;
import com.nebula.notescape.payload.response.UserResponse;
import com.nebula.notescape.persistence.dao.UserDao;
import com.nebula.notescape.persistence.entity.User;
import com.nebula.notescape.persistence.repository.UserRepository;
import com.nebula.notescape.persistence.specification.UserSpecification;
import com.nebula.notescape.security.JwtUtil;
import com.nebula.notescape.service.BaseService;
import com.nebula.notescape.service.IUserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl extends BaseService implements IUserService {

  private final UserDao userDao;
  private final UserSpecification userSpecification;
  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;

  @Override
  public ApiResponse getById(Long id) {
    if (id == null || id <= 0) {
      throw new ApiException(Exceptions.INVALID_ID);
    } else {
      Optional<User> userOptional = userDao.getById(id);

      if (userOptional.isEmpty()) {
        throw new ApiException(Exceptions.USER_NOT_FOUND);
      } else {
        return ApiResponse.builder()
            .status(HttpStatus.OK)
            .data(UserResponse.of(userOptional.get()))
            .build();
      }
    }
  }

  @Override
  public ApiResponse getByUsername(String username) {
    if (!StringUtils.hasText(username)) {
      throw new ApiException(Exceptions.INVALID_USERNAME);
    } else {
      Optional<User> userOptional = userDao.getByUsername(username);

      if (userOptional.isEmpty()) {
        throw new ApiException(username + " was not found");
      } else {
        return ApiResponse.builder()
            .status(HttpStatus.OK)
            .data(UserResponse.of(userOptional.get()))
            .build();
      }
    }
  }

  @Override
  public ApiResponse getUsersByKeyword(String keyword, Integer page, Integer size, String[] sort) {
    Pageable pageable = createPageable(page - 1, size, sort);
    Page<User> userPage = userDao.getByKeyword(keyword, pageable);
    return ApiResponse.builder()
        .status(HttpStatus.OK)
        .data(userPage.stream()
            .map(UserResponse::of)
            .toList())
        .put("page", page)
        .put("totalPages", userPage.getTotalPages())
        .put("size", userPage.getSize())
        .build();
  }

  @Override
  public ApiResponse update(String token, Long id, UserRequest request) {
    token = jwtUtil.parseToken(token);
    String email = jwtUtil.extractClaim(token, Claims::getSubject);

    if (request == null) {
      log.error("Null parameter userRequest");
      throw new ApiException(Exceptions.INVALID_REQUEST_BODY);
    }

    // Check if user exists
    Optional<User> userOpt = userDao.getByEmail(email);
    if (userOpt.isEmpty()) {
      throw new ApiException(Exceptions.USER_NOT_FOUND);
    }

    // we make sure emails match each other from request and token
    if (!email.equals(request.getEmail())) {
      throw new ApiException(Exceptions.INVALID_EMAIL);
    }

    // If so then update fields
    User user = userOpt.get();
    if (StringUtils.hasText(request.getUsername()) &&
        !request.getUsername().equals(user.getUsername())) {
      // When updating username we must check if it's possible
      if (userDao.existsByUsername(request.getUsername())) {
        throw new ApiException(Exceptions.USER_ALREADY_EXISTS);
      }

      if (request.getUsername().length() < 4 ||
          request.getUsername().length() > 30) {
        throw new ApiException(Exceptions.INVALID_USERNAME_LENGTH);
      } else {
        user.setUsername(request.getUsername());
      }
    }

    if (StringUtils.hasText(request.getFullName()) &&
        !request.getFullName().equals(user.getFullName())) {
      user.setFullName(request.getFullName());
    }

    if (!request.getAbout().equals(user.getAbout())) {
      if (request.getAbout().length() > 240) {
        throw new ApiException(Exceptions.INVALID_ABOUT_LENGTH);
      } else {
        user.setAbout(request.getAbout());
      }
    }

    if (StringUtils.hasText(request.getImgUrl()) &&
        !request.getImgUrl().equals(user.getImgUrl())) {
      user.setImgUrl(request.getImgUrl());
    }

    // After updating fields we save it in the repo
    user.setUpdateDate(LocalDateTime.now());
    user = userDao.update(user);

    // And return api response
    log.info("Updated user={}", user.toString());
    return ApiResponse.builder()
        .status(HttpStatus.OK)
        .data(UserResponse.of(user))
        .build();
  }

  @Override
  public ApiResponse deleteById(Long id) {
    if (id == null || id < 1) {
      throw new ApiException(Exceptions.INVALID_ID);
    } else if (!userDao.existsById(id)) {
      throw new ApiException(Exceptions.USER_NOT_FOUND);
    } else {
      userDao.deleteById(id);
      log.info("Deleted user by id={}", id);
      return ApiResponse.builder()
          .status(HttpStatus.OK)
          .data("Successfully deleted by id{%d}".formatted(id))
          .build();
    }
  }

  @Override
  public ApiResponse deleteByUsername(String username) {
    if (!StringUtils.hasText(username)) {
      throw new ApiException(Exceptions.INVALID_ID);
    } else if (!userDao.existsByUsername(username)) {
      throw new ApiException(username + " was not found");
    } else {
      userDao.deleteByUsername(username);
      return ApiResponse.builder()
          .status(HttpStatus.OK)
          .data("'%s' deleted successfully".formatted(username))
          .build();
    }
  }

}
