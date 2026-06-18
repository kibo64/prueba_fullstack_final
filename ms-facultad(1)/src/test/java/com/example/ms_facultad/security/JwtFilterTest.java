package com.example.ms_facultad.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    private JwtFilter filter;

    @BeforeEach
    void setUp() {
        filter = new JwtFilter(jwtUtil);
        SecurityContextHolder.clearContext();
    }

    @Test
    void testSinHeader() throws Exception {

        when(request.getServletPath())
                .thenReturn("/api/v1/test");

        when(request.getHeader("Authorization"))
                .thenReturn(null);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    void testTokenValido() throws Exception {

        when(request.getServletPath())
                .thenReturn("/api/v1/test");

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer token");

        when(jwtUtil.esValido("token"))
                .thenReturn(true);

        when(jwtUtil.obtenerUsuario("token"))
                .thenReturn("nico");

        when(jwtUtil.obtenerRole("token"))
                .thenReturn("ROLE_PROFESOR");

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    void testTokenInvalido() throws Exception {

        when(request.getServletPath())
                .thenReturn("/api/v1/test");

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer token");

        when(jwtUtil.esValido("token"))
                .thenReturn(false);

        PrintWriter writer = mock(PrintWriter.class);

        when(response.getWriter())
                .thenReturn(writer);

        filter.doFilter(request, response, chain);

        verify(response)
                .setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    void testShouldNotFilterSwagger() {

        when(request.getServletPath())
                .thenReturn("/swagger-ui/index.html");

        assertTrue(filter.shouldNotFilter(request));
    }

    @Test
    void testShouldFilterApi() {

        when(request.getServletPath())
                .thenReturn("/api/v1/test");

        assertFalse(filter.shouldNotFilter(request));
    }
}