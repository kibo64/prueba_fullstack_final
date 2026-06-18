package com.example.ms_cursos.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
@Data
public class CursoDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre del curso es obligatorio")
    private String nombreC;

    @NotNull(message = "La cantidad de estudiantes es obligatoria")
    @Min(value = 1, message = "Debe permitir al menos 1 estudiante")
    private Integer cantidadE;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 5, max = 100)
    private String descripcion;

    @NotNull(message = "El profesor es obligatorio")
    private Long idProfesor;

    @NotNull(message = "La facultad es obligatoria")
    private Long idFacultad;
}