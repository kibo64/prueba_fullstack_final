package com.example.ms_cursos.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.ms_cursos.dto.ApiResponse;
import com.example.ms_cursos.dto.ProfesorCursoResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProfesorClient {

    private final WebClient webClient;

    private final String BASE_URL = "http://localhost:8084/api/v1/profesores/";

    public ProfesorCursoResponse obtenerProfesor(Long id, String token) {

        ApiResponse<ProfesorCursoResponse> response = webClient.get()
                .uri(BASE_URL + id)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<ProfesorCursoResponse>>() {})
                .block();

        return response != null ? response.getData() : null;
    }
}