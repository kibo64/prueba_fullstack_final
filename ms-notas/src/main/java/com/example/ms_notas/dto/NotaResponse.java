package com.example.ms_notas.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotaResponse {

    private Long id;
    private Double nota;

    private EstudianteResponse estudiante;
    private EvaluacionResponse evaluacion;
}