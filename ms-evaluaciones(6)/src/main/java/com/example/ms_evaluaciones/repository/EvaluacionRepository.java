package com.example.ms_evaluaciones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ms_evaluaciones.model.Evaluacion;

public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {
List<Evaluacion> findByNombreEContainingIgnoreCase(String nombreE);

List<Evaluacion> findByIdCurso(Long idCurso);
}