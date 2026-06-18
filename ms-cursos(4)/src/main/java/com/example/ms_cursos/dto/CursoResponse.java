package com.example.ms_cursos.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CursoResponse {

    private Long id;
    private String nombreC;
    private Integer cantidadE;
    private String descripcion;
    private ProfesorCursoResponse profesor;
    private FacultadResponse facultad;
}