package com.example.ms_notas.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.ms_notas.dto.ApiResponse;
import com.example.ms_notas.dto.EvaluacionResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EvaluacionClient {

    private final WebClient webClient;

    private final String BASE_URL = "http://localhost:8087/api/v1/evaluaciones/";

    public EvaluacionResponse obtenerEvaluacion(Long id, String token) {

        ApiResponse<EvaluacionResponse> response = webClient.get()
                .uri(BASE_URL + id)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<EvaluacionResponse>>() {})
                .block();

        return response != null ? response.getData() : null;
    }
}