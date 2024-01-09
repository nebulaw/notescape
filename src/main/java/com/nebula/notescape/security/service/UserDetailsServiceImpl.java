package com.nebula.notescape.security.service;

import com.nebula.notescape.jpa.RecordState;
import com.nebula.notescape.jpa.entity.User;
import com.nebula.notescape.jpa.repository.UserRepository;
import com.nebula.notescape.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository
                .findByUsernameAndRecordState(username, RecordState.ACTIVE);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("%s was not found"
                    .formatted(username));
        } else {
            return UserDetailsImpl.builder()
                    .username(userOptional.get().getPassword())
                    .password(userOptional.get().getPassword())
                    .authority(userOptional.get().getAuthority())
                    .build();
        }
    }

}
