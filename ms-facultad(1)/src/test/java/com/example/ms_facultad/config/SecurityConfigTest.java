package com.example.ms_facultad.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import com.example.ms_facultad.security.JwtFilter;

import static org.mockito.Mockito.*;

class SecurityConfigTest {

    @Test
    void testBeans() throws Exception {

        JwtFilter jwtFilter = mock(JwtFilter.class);

        SecurityConfig config = new SecurityConfig(jwtFilter);

        assertNotNull(config.accessDeniedHandler());
        assertNotNull(config.authenticationEntryPoint());

        HttpSecurity http = mock(HttpSecurity.class);
        try {
            config.filterChain(http);
        } catch (Exception ignored) {
        }
    }
}