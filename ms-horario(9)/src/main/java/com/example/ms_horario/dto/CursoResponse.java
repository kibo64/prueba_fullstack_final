package com.example.ms_horario.dto;


import lombok.Data;

@Data
public class CursoResponse {

    private Long id;
    private String nombreC;
    private Integer cantidadE;
    private String descripcion;
    private ProfesorCursoResponse profesor;
}