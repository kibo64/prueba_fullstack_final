package com.example.ms_estudiantes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EstudianteResponse {

    private Long id;
    private String rut;
    private String nombre;
    private String email;
    private String carrera;

    private FacultadResponse facultad;
}
