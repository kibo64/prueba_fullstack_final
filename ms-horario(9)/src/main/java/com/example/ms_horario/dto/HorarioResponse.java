package com.example.ms_horario.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HorarioResponse {

    private Long id;
    private String dia;
    private String horaInicio;
    private String horaFin;
    private String sala;

    private CursoResponse curso;
}