package com.example.ms_cursos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ms_cursos.model.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {
List<Curso> findByNombreCContainingIgnoreCase(String nombreC);

List<Curso> findByIdProfesor(Long idProfesor);

List<Curso> findByIdFacultad(Long idFacultad);
}