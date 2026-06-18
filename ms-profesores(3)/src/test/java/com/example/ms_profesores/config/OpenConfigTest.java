package com.example.ms_profesores.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class OpenConfigTest {

    @Test
    void openApiTest() {

        OpenApiConfig config = new OpenApiConfig();

        assertNotNull(config.customOpenAPI());
    }
}