package com.example.ms_notas.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double nota;

    private Long idEstudiante;

    private Long idEvaluacion;
}