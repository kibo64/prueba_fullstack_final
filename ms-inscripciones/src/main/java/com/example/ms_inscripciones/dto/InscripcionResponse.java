package com.example.ms_inscripciones.dto;

import java.time.LocalDate;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscripcionResponse {

    private Long id;

    private LocalDate fecha;

    private EstudianteResponse estudiante;

    private CursoResponse curso;
}