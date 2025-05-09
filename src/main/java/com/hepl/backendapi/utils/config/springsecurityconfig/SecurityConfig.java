package com.hepl.backendapi.utils.config.springsecurityconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity()
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomJwtAuthenticationConverter converter;
    private final JwtDecoder jwtDecoder;

    public SecurityConfig(CustomJwtAuthenticationConverter converter, JwtDecoder jwtDecoder) {
        this.converter = converter;
        this.jwtDecoder = jwtDecoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // on désactive le CSRF car on utilise un JWT
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/",
                                "/ws/**", // autorisation du handshake WebSocket
                                "/v1/user"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(converter)
                                .decoder(jwtDecoder)
                        )
                )
                .build();
    }
}
