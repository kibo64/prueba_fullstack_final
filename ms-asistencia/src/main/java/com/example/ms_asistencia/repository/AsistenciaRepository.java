package com.example.ms_asistencia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ms_asistencia.model.Asistencia;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
List<Asistencia> findByIdEstudiante(Long idEstudiante);

List<Asistencia> findByIdCurso(Long idCurso);

List<Asistencia> findByIdEstudianteAndIdCurso(Long idEstudiante, Long idCurso);
}