package com.example.ms_evaluaciones.dto;

import java.time.LocalDate;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluacionResponse {

    private Long id;

    private String nombreE;

    private LocalDate fecha;

    private Double ponderacion;

    private CursoResponse curso;
}