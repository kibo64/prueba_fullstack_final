package com.example.ms_historial_academico.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "historial_academico")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialAcademico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double promedioFinal;

    private Double asistenciaFinal;

    private String estado;

    private Long idEstudiante;

    private Long idCurso;
}