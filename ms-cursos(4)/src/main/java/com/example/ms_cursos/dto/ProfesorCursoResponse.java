package com.example.ms_cursos.dto;

import lombok.Data;

@Data
public class ProfesorCursoResponse {

    private Long id;
    private String rutP;
    private String nombreP;
    private String emailP;
    private String especialidad;
}