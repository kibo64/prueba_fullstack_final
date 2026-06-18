package com.example.ms_profesores.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ms_profesores.model.Profesor;

public interface ProfesorRepository extends JpaRepository<Profesor, Long>{
    List<Profesor> findByNombrePContainingIgnoreCase(String nombreP);

    List<Profesor> findByEspecialidadContainingIgnoreCase(String especialidad);

    List<Profesor> findByIdFacultad(Long idFacultad);
}
