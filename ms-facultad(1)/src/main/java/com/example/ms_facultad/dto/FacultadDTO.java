package com.example.ms_facultad.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FacultadDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "La ubicación es obligatoria")
    @Size(min = 3, max = 50, message = "La ubicación debe tener entre 3 y 50 caracteres")
    private String ubicacion;
}