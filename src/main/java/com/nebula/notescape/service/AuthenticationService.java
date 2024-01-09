package com.nebula.notescape.service;

import com.nebula.notescape.jpa.Authority;
import com.nebula.notescape.jpa.RecordState;
import com.nebula.notescape.jpa.entity.User;
import com.nebula.notescape.jpa.repository.UserRepository;
import com.nebula.notescape.payload.request.LoginRequest;
import com.nebula.notescape.payload.request.RegisterRequest;
import com.nebula.notescape.payload.response.ApiResponse;
import com.nebula.notescape.payload.response.AuthResponse;
import com.nebula.notescape.payload.response.UserResponse;
import com.nebula.notescape.security.UserDetailsImpl;
import com.nebula.notescape.exception.UserNotFoundException;
import com.nebula.notescape.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final UserRepository userRepository;
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
        Optional<User> userOptional = userRepository
                .findByEmailAndRecordState(
                        userDetails.getUsername(),
                        RecordState.ACTIVE
                );

        if (userOptional.isEmpty()) {
            log.error("{} not found in the user repository", loginRequest.getEmail());
            throw new UserNotFoundException("%s not found"
                    .formatted(userDetails.getUsername()));
        } else {
            log.trace("Successfully retrieved {} from the user repository", loginRequest.getEmail());
            log.info("{} authenticated successfully", loginRequest.getEmail());
            return AuthResponse.builder()
                    .user(UserResponse.of(userOptional.get()))
                    .token(jwt)
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
        user = userRepository.save(user);
        log.debug("Successfully saved {} in the user repository", user.getEmail());
        log.info("{} registered successfully", registerRequest.getEmail());

        // Generate token
        UserDetails userDetails = UserDetailsImpl.of(user);
        final String jwt = jwtUtil.generateToken(userDetails);
        log.trace("Successfully generated token for {}", registerRequest.getEmail());

        log.info("{} authenticated successfully", registerRequest.getEmail());
        return AuthResponse.builder()
                .user(UserResponse.of(user))
                .token(jwt)
                .build();
    }

}
