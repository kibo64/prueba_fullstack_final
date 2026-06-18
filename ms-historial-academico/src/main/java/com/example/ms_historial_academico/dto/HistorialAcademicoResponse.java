package com.example.ms_historial_academico.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialAcademicoResponse {

    private Long id;
    private Double promedioFinal;
    private Double asistenciaFinal;
    private String estado;

    private EstudianteResponse estudiante;
    private CursoResponse curso;
}