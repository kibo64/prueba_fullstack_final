package com.example.ms_asistencia.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.ms_asistencia.dto.ApiResponse;
import com.example.ms_asistencia.dto.CursoResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CursoClient {

    private final WebClient webClient;

    private final String BASE_URL = "http://localhost:8085/api/v1/cursos/";

    public CursoResponse obtenerCurso(Long id, String token) {

        ApiResponse<CursoResponse> response = webClient.get()
                .uri(BASE_URL + id)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<CursoResponse>>() {})
                .block();

        return response != null ? response.getData() : null;
    }
}