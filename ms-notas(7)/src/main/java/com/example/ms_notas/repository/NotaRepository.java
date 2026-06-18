package com.example.ms_notas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ms_notas.model.Nota;

public interface NotaRepository extends JpaRepository<Nota, Long> {
List<Nota> findByIdEstudiante(Long idEstudiante);

List<Nota> findByIdEvaluacion(Long idEvaluacion);
}