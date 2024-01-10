package com.nebula.notescape.security;

import com.nebula.notescape.persistence.RecordState;
import com.nebula.notescape.persistence.entity.User;
import com.nebula.notescape.persistence.repository.UserRepository;
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
                .findByEmailAndRecordState(username, RecordState.ACTIVE);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("%s was not found"
                    .formatted(username));
        } else {
            return UserDetailsImpl.builder()
                    .email(userOptional.get().getPassword())
                    .password(userOptional.get().getPassword())
                    .authority(userOptional.get().getAuthority())
                    .build();
        }
    }

}
