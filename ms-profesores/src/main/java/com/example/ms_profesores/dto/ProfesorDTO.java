package com.example.ms_profesores.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProfesorDTO {
    @NotBlank(message = "El rut es obligatorio")
    @Size(min = 9, max = 12, message = "El RUT debe tener entre 9 y 12 caracteres")
    private String rutP;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String nombreP;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un @")
    private String emailP;

    @NotBlank(message = "La especialidad es obligatoria")
    private String especialidad;

    @NotNull(message = "La facultad es obligatoria")
    private Long idFacultad;
}
