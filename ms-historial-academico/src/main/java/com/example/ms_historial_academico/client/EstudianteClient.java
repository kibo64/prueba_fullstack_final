package com.example.ms_historial_academico.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.ms_historial_academico.dto.ApiResponse;
import com.example.ms_historial_academico.dto.EstudianteResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EstudianteClient {

    private final WebClient webClient;

    private final String BASE_URL = "http://ms-estudiantes:8083/api/v1/estudiantes/";

    public EstudianteResponse obtenerEstudiante(Long id, String token) {

        ApiResponse<EstudianteResponse> response = webClient.get()
                .uri(BASE_URL + id)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<EstudianteResponse>>() {})
                .block();

        return response != null ? response.getData() : null;
    }
}