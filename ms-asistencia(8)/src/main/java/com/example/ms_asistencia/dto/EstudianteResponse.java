package com.example.ms_asistencia.dto;

import lombok.Data;

@Data
public class EstudianteResponse {

    private Long id;

    private String rut;

    private String nombre;

    private String email;

    private String carrera;
}