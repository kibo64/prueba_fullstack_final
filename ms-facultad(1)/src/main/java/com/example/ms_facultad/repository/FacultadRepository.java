package com.example.ms_facultad.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ms_facultad.model.Facultad;

public interface FacultadRepository extends JpaRepository<Facultad, Long> {

    List<Facultad> findByNombreContainingIgnoreCase(String nombre);
    List<Facultad> findByUbicacionContainingIgnoreCase(String ubicacion);
}