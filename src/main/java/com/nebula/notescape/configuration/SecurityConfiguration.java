package com.nebula.notescape.configuration;

import com.nebula.notescape.security.JwtAuthenticationFilter;
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
        .authorizeHttpRequests(authManager -> {
          authManager
              .requestMatchers("/api/auth/**").permitAll()
              .requestMatchers("/api/users/find/*").permitAll()
              .requestMatchers("/api/users/discover").permitAll()
              .requestMatchers("/api/users/update").authenticated();

          authManager
              .requestMatchers("/api/notes/find/*").permitAll()
              .requestMatchers("/api/notes/create").authenticated()
              .requestMatchers("/api/notes/delete/*").authenticated()
              .requestMatchers("/api/notes/private").authenticated()
              .requestMatchers("/api/notes/user").authenticated()
              .requestMatchers("/api/notes/discover/**").permitAll();

          authManager
              .requestMatchers("/api/interactions/**").authenticated();

          authManager
              .requestMatchers("/actuator/**")
              .permitAll();

          authManager.anyRequest().authenticated();
        })
        .passwordManagement(pm -> {
          pm.changePasswordPage("/api/users/update-password");
        })
        .exceptionHandling(exceptions -> {
//                     exceptions
//                            .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
//                            .accessDeniedHandler(new BearerTokenAccessDeniedHandler());
        })
        .addFilterBefore(jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class)
        .build();
  }

}
