package com.example.ms_historial_academico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ms_historial_academico.model.HistorialAcademico;

public interface HistorialAcademicoRepository extends JpaRepository<HistorialAcademico, Long> {
    List<HistorialAcademico> findByIdEstudiante(Long idEstudiante);

    List<HistorialAcademico> findByIdCurso(Long idCurso);

    List<HistorialAcademico> findByEstadoIgnoreCase(String estado);
}