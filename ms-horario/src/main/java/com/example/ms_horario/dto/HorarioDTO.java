package com.example.ms_horario.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class HorarioDTO {

    @NotBlank(message = "El día es obligatorio")
    private String dia;

    @NotBlank(message = "La hora de inicio es obligatoria")
    private String horaInicio;

    @NotBlank(message = "La hora de fin es obligatoria")
    private String horaFin;

    @NotBlank(message = "La sala es obligatoria")
    @Size(min = 2, max = 30, message = "La sala debe tener entre 2 y 30 caracteres")
    private String sala;

    @NotNull(message = "El curso es obligatorio")
    private Long idCurso;
}