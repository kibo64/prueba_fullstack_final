package com.example.ms_evaluaciones.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.*;
import lombok.Data;
@Data
public class EvaluacionDTO {

    @NotBlank(message = "El nombre de la evaluación es obligatorio")
    private String nombreE;

    @NotNull(message = "La fecha es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    @NotNull(message = "La ponderación es obligatoria")
    @DecimalMin(value = "1.0", message = "La ponderación mínima es 1")
    @DecimalMax(value = "100.0", message = "La ponderación máxima es 100")
    private Double ponderacion;

    @NotNull(message = "El curso es obligatorio")
    private Long idCurso;
}