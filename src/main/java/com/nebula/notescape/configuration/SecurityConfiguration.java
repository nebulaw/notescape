package com.nebula.notescape.configuration;

import com.nebula.notescape.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // Disable CSRF(Cross-Site Request Forgery) because of JWT Auth
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/auth/**").permitAll();
                    auth.requestMatchers("/api/users/find/*").permitAll();
                    auth.anyRequest().authenticated();
                })
                .passwordManagement(pm -> {
                    pm.changePasswordPage("/api/users/update-password");
                })
                .exceptionHandling(exceptions -> {
                    // Handling exceptions...
                    // exceptions
                    //        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                    //        .accessDeniedHandler(new BearerTokenAccessDeniedHandler());
                })
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
