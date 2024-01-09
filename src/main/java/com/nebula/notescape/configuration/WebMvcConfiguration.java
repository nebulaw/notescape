package com.nebula.notescape.configuration;

import io.micrometer.common.lang.NonNullApi;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfiguration {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        String allowedOrigin = "http://localhost:3000";

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                // Configure for user controller
                registry.addMapping("/api/v1/users/**")
                        .allowedOrigins(allowedOrigin)
                        .allowedMethods("*");
                registry.addMapping("/api/v1/notes/**")
                        .allowedOrigins(allowedOrigin)
                        .allowedMethods("*");
            }
        };
    }


}
