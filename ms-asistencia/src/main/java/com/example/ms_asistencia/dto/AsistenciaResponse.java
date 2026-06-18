package com.example.ms_asistencia.dto;

import java.time.LocalDate;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsistenciaResponse {

    private Long id;
    private LocalDate fechaClase;
    private Boolean asistencia;

    private EstudianteResponse estudiante;
    private CursoResponse curso;
}