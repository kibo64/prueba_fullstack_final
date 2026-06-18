package com.example.ms_horario.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "horarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dia;

    private String horaInicio;

    private String horaFin;

    private String sala;

    private Long idCurso;
}