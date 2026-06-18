package com.example.ms_historial_academico.dto;

import lombok.*;

@Data
public class CursoResponse {

    private Long id;
    private String nombreC;
    private Integer cantidadE;
    private String descripcion;
    private ProfesorCursoResponse profesor;
}