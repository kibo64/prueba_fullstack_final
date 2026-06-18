package com.example.ms_cursos.security;

import static org.junit.jupiter.api.Assertions.*;

import java.security.Key;
import java.util.Date;

import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

class JwtUtilTest {

    private final String secret =
            "12345678901234567890123456789012";

    @Test
    void testTokenInvalido() {

        JwtUtil jwtUtil = new JwtUtil(secret);

        assertFalse(jwtUtil.esValido("token_invalido"));
    }

    @Test
    void testTokenValido() {

        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        String token = Jwts.builder()
                .setSubject("nico")
                .claim("role", "ROLE_PROFESOR")
                .setExpiration(new Date(System.currentTimeMillis() + 60000))
                .signWith(key)
                .compact();

        JwtUtil jwtUtil = new JwtUtil(secret);

        assertTrue(jwtUtil.esValido(token));
        assertEquals("nico", jwtUtil.obtenerUsuario(token));
        assertEquals("ROLE_PROFESOR", jwtUtil.obtenerRole(token));
    }
}