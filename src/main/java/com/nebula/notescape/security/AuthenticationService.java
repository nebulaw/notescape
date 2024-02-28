package com.nebula.notescape.security;

import com.nebula.notescape.exception.ApiException;
import com.nebula.notescape.exception.CustomMessageException;
import com.nebula.notescape.payload.request.LoginRequest;
import com.nebula.notescape.payload.request.RegisterRequest;
import com.nebula.notescape.payload.response.ApiResponse;
import com.nebula.notescape.payload.response.AuthResponse;
import com.nebula.notescape.persistence.dao.UserDao;
import com.nebula.notescape.persistence.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationService {

  private final AuthenticationManager authenticationManager;
  private final UserDao userDao;
  private final PasswordEncoder encoder;
  private final JwtUtil jwtUtil;

  public ApiResponse login(LoginRequest loginRequest) {
    validateLoginRequest(loginRequest);

    // Authenticate by email and password
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.getEmail(),
            loginRequest.getPassword()
        )
    );
    log.info("AuthenticationManager successfully authenticated {}", loginRequest.getEmail());

    // Generate authentication token
    UserDetails userDetails = UserDetailsImpl.of(loginRequest);
    final String jwt = jwtUtil.generateToken(userDetails);
    log.trace("Successfully generated token for {}", loginRequest.getEmail());

    // Grab information about user
    Optional<User> userOptional = userDao.getByEmail(userDetails.getUsername());

    if (userOptional.isEmpty()) {
      log.error("{} not found in the user repository", loginRequest.getEmail());
      throw new ApiException(userDetails.getUsername() + " was not found.");
    } else {
      log.trace("Successfully retrieved {} from the user repository", loginRequest.getEmail());
      log.info("{} authenticated successfully", loginRequest.getEmail());
      return ApiResponse.builder()
          .data(new AuthResponse(userOptional.get(), jwt))
          .status(HttpStatus.OK)
          .build();
    }
  }

  public ApiResponse register(RegisterRequest registerRequest) {
    validateRegisterRequest(registerRequest);

    // encode password
    registerRequest.setPassword(encoder.encode(registerRequest.getPassword()));
    // Build user from register request
    User user = User.of(registerRequest);

    // Save it in the user repository
    user = userDao.save(user);
    log.debug("Successfully saved {} in the user repository", user.getEmail());
    log.info("{} registered successfully", registerRequest.getEmail());

    // Generate token
    UserDetails userDetails = UserDetailsImpl.of(user);
    final String jwt = jwtUtil.generateToken(userDetails);
    log.trace("Successfully generated token for {}", registerRequest.getEmail());

    log.info("{} authenticated successfully", registerRequest.getEmail());
    return ApiResponse.builder()
        .data(new AuthResponse(user, jwt))
        .status(HttpStatus.CREATED)
        .build();
  }

  private void validateLoginRequest(LoginRequest loginRequest) {
    List<String> errors = new ArrayList<>();

    if (!StringUtils.hasText(loginRequest.getEmail())) {
      errors.add("Email not provided");
    } else {
      Matcher matcher = Pattern
          .compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
          .matcher(loginRequest.getEmail());
      if (!matcher.find()) {
        errors.add("Invalid email");
      }
    }

    if (!StringUtils.hasText(loginRequest.getPassword())) {
      errors.add("Password not provided");
    } else if (loginRequest.getPassword().length() > 16 ||
        loginRequest.getPassword().length() < 8) {
      errors.add("Password length must be 8-16 characters");
    }

    if (!errors.isEmpty()) {
      throw new CustomMessageException(errors, HttpStatus.BAD_REQUEST);
    }
  }

  private void validateRegisterRequest(RegisterRequest registerRequest) {
    List<String> errors = new ArrayList<>();

    // validate email
    if (!StringUtils.hasText(registerRequest.getEmail())) {
      errors.add("Email not provided");
    } else {
      Matcher matcher = Pattern
          .compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
          .matcher(registerRequest.getEmail());
      if (!matcher.find()) {
        errors.add("Invalid email");
      }
    }

    if (!StringUtils.hasText(registerRequest.getUsername())) {
      errors.add("Username not provided");
    } else if (registerRequest.getUsername().length() > 30 ||
        registerRequest.getUsername().length() < 4) {
      errors.add("Username length must be 4-30 characters");
    }

    if (!StringUtils.hasText(registerRequest.getPassword())) {
      errors.add("Password not provided");
    } else if (registerRequest.getPassword().length() > 16 ||
        registerRequest.getPassword().length() < 8) {
      errors.add("Password length must be 8-16 characters");
    }

    if (!errors.isEmpty()) {
      throw new CustomMessageException(errors, HttpStatus.BAD_REQUEST);
    }
  }

}
