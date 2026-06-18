package com.example.ms_notas.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluacionResponse {

    private Long id;

    private String nombreE;

    private String fecha;

    private Double ponderacion;

    private CursoResponse curso;
}