package com.example.ms_asistencia.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AsistenciaDTO {

    @NotNull(message = "La fecha de clase es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaClase;

    @NotNull(message = "La asistencia es obligatoria")
    private Boolean asistencia;

    @NotNull(message = "El estudiante es obligatorio")
    private Long idEstudiante;

    @NotNull(message = "El curso es obligatorio")
    private Long idCurso;
}