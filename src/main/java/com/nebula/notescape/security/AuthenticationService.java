package com.nebula.notescape.security;

import com.nebula.notescape.exception.UserNotFoundException;
import com.nebula.notescape.payload.request.LoginRequest;
import com.nebula.notescape.payload.request.RegisterRequest;
import com.nebula.notescape.payload.response.ApiResponse;
import com.nebula.notescape.payload.response.AuthResponse;
import com.nebula.notescape.payload.response.UserResponse;
import com.nebula.notescape.persistence.Authority;
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

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserDao userDao;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public ApiResponse login(LoginRequest loginRequest) {
        // TODO: validate login request

        // Authenticate by email and password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        log.debug("AuthenticationManager successfully authenticated {}", loginRequest.getEmail());
        // NOTE: Login request password encoding?
        // loginRequest.setPassword(encoder.encode(loginRequest.getPassword()));

        // Generate authentication token
        UserDetails userDetails = UserDetailsImpl.of(loginRequest);
        final String jwt = jwtUtil.generateToken(userDetails);
        log.trace("Successfully generated token for {}", loginRequest.getEmail());

        // Grab information about user
        Optional<User> userOptional = userDao.getByEmail(userDetails.getUsername());

        if (userOptional.isEmpty()) {
            log.error("{} not found in the user repository", loginRequest.getEmail());
            throw new UserNotFoundException(userDetails);
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
        // TODO: validate register request

        // Build user from register request
        User user = User.builder()
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .fullName(registerRequest.getFullName())
                .password(encoder.encode(registerRequest.getPassword()))
                .authority(Authority.USER)
                .imgUrl("")
                .build();

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

}
