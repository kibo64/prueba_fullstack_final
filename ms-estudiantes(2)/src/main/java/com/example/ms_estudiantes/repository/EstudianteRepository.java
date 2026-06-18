package com.example.ms_estudiantes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ms_estudiantes.model.Estudiante;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
List<Estudiante> findByNombreContainingIgnoreCase(String nombre);

List<Estudiante> findByCarreraContainingIgnoreCase(String carrera);

List<Estudiante> findByIdFacultad(Long idFacultad);
}