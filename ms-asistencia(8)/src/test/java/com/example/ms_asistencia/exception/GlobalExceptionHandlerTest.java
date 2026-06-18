package com.example.ms_asistencia.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.example.ms_asistencia.dto.ApiResponse;

import jakarta.persistence.EntityNotFoundException;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler =
            new GlobalExceptionHandler();

    @Test
    void testHandleRuntime() {

        ResponseEntity<ApiResponse<Object>> response =
                handler.handleNotFound(
                        new RuntimeException("error")
                );

        assertEquals(404,
                response.getStatusCode().value());
    }

    @Test
    void testHandle404() {

        ResponseEntity<ApiResponse<Object>> response =
                handler.handle404(
                        new EntityNotFoundException("no existe")
                );

        assertEquals(404,
                response.getStatusCode().value());
    }

    @Test
    void testHandleGeneral() {

        ResponseEntity<ApiResponse<Object>> response =
                handler.handleGeneral(
                        new Exception("error")
                );

        assertEquals(500,
                response.getStatusCode().value());
    }
}