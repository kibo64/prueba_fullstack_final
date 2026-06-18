package com.example.ms_cursos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cursos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreC;

    private Integer cantidadE;

    private String descripcion;

    private Long idProfesor;
    private Long idFacultad;
}