package com.nebula.notescape.security;

import com.nebula.notescape.persistence.Authority;
import com.nebula.notescape.persistence.entity.User;
import com.nebula.notescape.payload.request.LoginRequest;
import lombok.Builder;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Builder
@ToString
public class UserDetailsImpl implements UserDetails {

    private String email;
    private String password;
    private Authority authority;

    public static UserDetailsImpl of(LoginRequest loginRequest) {
        return UserDetailsImpl.builder()
                .email(loginRequest.getEmail())
                .password(loginRequest.getPassword())
                .authority(Authority.USER)
                .build();
    }

    public static UserDetailsImpl of(User user) {
        return UserDetailsImpl.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .authority(user.getAuthority())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
