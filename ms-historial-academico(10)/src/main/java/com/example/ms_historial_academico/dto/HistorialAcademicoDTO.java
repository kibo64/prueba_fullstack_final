package com.example.ms_historial_academico.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class HistorialAcademicoDTO {

    @NotNull(message = "El promedio final es obligatorio")
    @DecimalMin(value = "1.0", message = "El promedio mínimo es 1.0")
    @DecimalMax(value = "7.0", message = "El promedio máximo es 7.0")
    private Double promedioFinal;

    @NotNull(message = "La asistencia final es obligatoria")
    @DecimalMin(value = "0.0", message = "La asistencia mínima es 0")
    @DecimalMax(value = "100.0", message = "La asistencia máxima es 100")
    private Double asistenciaFinal;

    @NotNull(message = "El estudiante es obligatorio")
    private Long idEstudiante;

    @NotNull(message = "El curso es obligatorio")
    private Long idCurso;
}