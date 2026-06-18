package com.example.ms_cursos.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.ms_cursos.dto.ApiResponse;
import com.example.ms_cursos.dto.FacultadResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FacultadClient {

    private final WebClient webClient;

    private final String BASE_URL = "http://ms-facultad:8082/api/v1/facultades/";

    public FacultadResponse obtenerFacultad(Long id, String token) {

        ApiResponse<FacultadResponse> response = webClient.get()
                .uri(BASE_URL + id)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<FacultadResponse>>() {})
                .block();

        return response != null ? response.getData() : null;
    }
}