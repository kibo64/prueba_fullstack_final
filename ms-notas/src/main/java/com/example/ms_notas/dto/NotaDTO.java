package com.example.ms_notas.dto;

import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class NotaDTO {

    @NotNull(message = "La nota es obligatoria")
    @DecimalMin(value = "1.0", message = "La nota mínima es 1.0")
    @DecimalMax(value = "7.0", message = "La nota máxima es 7.0")
    private Double nota;

    @NotNull(message = "El estudiante es obligatorio")
    private Long idEstudiante;

    @NotNull(message = "La evaluación es obligatoria")
    private Long idEvaluacion;
}
