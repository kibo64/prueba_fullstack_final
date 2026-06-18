package com.example.ms_estudiantes.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EstudianteDTO {

    @NotBlank(message = "El rut es obligatorio")
    @Size(min = 9, max = 12, message = "El RUT debe tener entre 9 y 12 caracteres")
    private String rut;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un @")
    private String email;

    @NotBlank(message = "La carrera es obligatoria")
    private String carrera;

    @NotNull(message = "La facultad es obligatoria")
    private Long idFacultad;
}