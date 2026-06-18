package com.example.ms_estudiantes.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ms_estudiantes.client.FacultadClient;
import com.example.ms_estudiantes.dto.EstudianteDTO;
import com.example.ms_estudiantes.dto.EstudianteResponse;
import com.example.ms_estudiantes.model.Estudiante;
import com.example.ms_estudiantes.repository.EstudianteRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstudianteService {

    private final EstudianteRepository repo;
    private final FacultadClient facultadClient;

    public EstudianteResponse crear(EstudianteDTO dto, String token) {

        log.info("Creando estudiante: {}", dto.getNombre());

        var facultad = facultadClient.obtenerFacultad(dto.getIdFacultad(), token);

        if (facultad == null) {
            log.error("Facultad {} no existe", dto.getIdFacultad());
            throw new RuntimeException("Facultad no existe");
        }

        Estudiante estudiante = repo.save(
                new Estudiante(
                        null,
                        dto.getRut(),
                        dto.getNombre(),
                        dto.getEmail(),
                        dto.getCarrera(),
                        dto.getIdFacultad()
                )
        );

        log.info("Estudiante creado con id {}", estudiante.getId());

        return mapToResponse(estudiante, token);
    }

    public List<EstudianteResponse> listar(String token) {

        log.info("Listando estudiantes");

        return repo.findAll()
                .stream()
                .map(e -> mapToResponse(e, token))
                .toList();
    }   

    public EstudianteResponse obtener(Long id, String token) {

        log.info("Buscando estudiante con id {}", id);

        Estudiante estudiante = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estudiante no encontrado"));

        return mapToResponse(estudiante, token);
    }

    public EstudianteResponse actualizar(Long id, EstudianteDTO dto, String token) {

        log.info("Actualizando estudiante con id {}", id);

        Estudiante e = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estudiante no encontrado"));

        e.setRut(dto.getRut());
        e.setNombre(dto.getNombre());
        e.setEmail(dto.getEmail());
        e.setCarrera(dto.getCarrera());
        e.setIdFacultad(dto.getIdFacultad());

        log.info("Estudiante actualizado con id {}", id);

        return mapToResponse(repo.save(e), token);
    }

    public void eliminar(Long id) {

        log.info("Eliminando estudiante con id {}", id);

        repo.deleteById(id);
    }

    public List<EstudianteResponse> buscarPorNombre(String nombre, String token) {

        log.info("Buscando estudiantes por nombre {}", nombre);

        return repo.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(e -> mapToResponse(e, token))
                .toList();
    }

    public List<EstudianteResponse> buscarPorCarrera(String carrera, String token) {

        log.info("Buscando estudiantes por carrera {}", carrera);

        return repo.findByCarreraContainingIgnoreCase(carrera)
                .stream()
                .map(e -> mapToResponse(e, token))
                .toList();
    }

    public List<EstudianteResponse> buscarPorFacultad(Long idFacultad, String token) {

        log.info("Buscando estudiantes por facultad {}", idFacultad);

        return repo.findByIdFacultad(idFacultad)
                .stream()
                .map(e -> mapToResponse(e, token))
                .toList();
    }
   
    private EstudianteResponse mapToResponse(Estudiante estudiante, String token) {

        log.debug("Mapeando estudiante {} a response", estudiante.getId());
        var facultad = facultadClient.obtenerFacultad(estudiante.getIdFacultad(), token);

        return EstudianteResponse.builder()
                .id(estudiante.getId())
                .rut(estudiante.getRut())
                .nombre(estudiante.getNombre())
                .email(estudiante.getEmail())
                .carrera(estudiante.getCarrera())
                .facultad(facultad)
                .build();
    }
}