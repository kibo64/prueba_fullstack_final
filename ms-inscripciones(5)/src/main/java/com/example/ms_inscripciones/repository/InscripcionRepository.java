package com.example.ms_inscripciones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ms_inscripciones.model.Inscripcion;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
List<Inscripcion> findByIdEstudiante(Long idEstudiante);

List<Inscripcion> findByIdCurso(Long idCurso);

boolean existsByIdEstudianteAndIdCurso(Long idEstudiante, Long idCurso);
}