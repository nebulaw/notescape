package com.nebula.notescape.security;

import com.nebula.notescape.persistence.dao.UserDao;
import com.nebula.notescape.persistence.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userDao.getByEmail(username);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("%s was not found"
                    .formatted(username));
        } else {
            return UserDetailsImpl.builder()
                    .email(userOptional.get().getEmail())
                    .password(userOptional.get().getPassword())
                    .authority(userOptional.get().getAuthority())
                    .build();
        }
    }

}
