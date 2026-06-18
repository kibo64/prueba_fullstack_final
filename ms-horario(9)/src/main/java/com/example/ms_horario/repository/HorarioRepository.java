package com.example.ms_horario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ms_horario.model.Horario;

public interface HorarioRepository extends JpaRepository<Horario, Long> {
List<Horario> findByIdCurso(Long idCurso);

List<Horario> findByDiaIgnoreCase(String dia);
}