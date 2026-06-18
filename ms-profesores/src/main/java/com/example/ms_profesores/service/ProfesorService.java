package com.example.ms_profesores.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ms_profesores.client.FacultadClient;
import com.example.ms_profesores.dto.ProfesorDTO;
import com.example.ms_profesores.dto.ProfesorResponse;
import com.example.ms_profesores.model.Profesor;
import com.example.ms_profesores.repository.ProfesorRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfesorService {

    private final ProfesorRepository repo;
    private final FacultadClient facultadClient;

    public ProfesorResponse crear(ProfesorDTO dto, String token) {

        log.info("Creando profesor: {}", dto.getNombreP());

        var facultad = facultadClient.obtenerFacultad(dto.getIdFacultad(), token);

        if (facultad == null) {
            log.error("Facultad {} no existe", dto.getIdFacultad());
            throw new RuntimeException("Facultad no existe");
        }

        Profesor profesor = repo.save(
                new Profesor(
                        null,
                        dto.getRutP(),
                        dto.getNombreP(),
                        dto.getEmailP(),
                        dto.getEspecialidad(),
                        dto.getIdFacultad()
                )
        );

        log.info("Profesor creado con id {}", profesor.getId());

        return mapToResponse(profesor, token);
    }

    public List<ProfesorResponse> listar(String token) {

        log.info("Listando profesores");

        return repo.findAll()
                .stream()
                .map(p -> mapToResponse(p, token))
                .toList();
    }

    public ProfesorResponse obtener(Long id, String token) {

        log.info("Buscando profesor con id {}", id);

        Profesor profesor = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Profesor no encontrado"));

        return mapToResponse(profesor, token);
    }

    public ProfesorResponse actualizar(Long id, ProfesorDTO dto, String token) {

        log.info("Actualizando profesor con id {}", id);

        Profesor p = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Profesor no encontrado"));

        p.setRutP(dto.getRutP());
        p.setNombreP(dto.getNombreP());
        p.setEmailP(dto.getEmailP());
        p.setEspecialidad(dto.getEspecialidad());
        p.setIdFacultad(dto.getIdFacultad());

        log.info("Profesor actualizado con id {}", id);

        return mapToResponse(repo.save(p), token);
    }

    public void eliminar(Long id) {

        log.info("Eliminando profesor con id {}", id);

        repo.deleteById(id);
    }

    public List<ProfesorResponse> buscarPorNombre(String nombreP, String token) {

        log.info("Buscando profesores por nombre {}", nombreP);

        return repo.findByNombrePContainingIgnoreCase(nombreP)
                .stream()
                .map(p -> mapToResponse(p, token))
                .toList();
    }

    public List<ProfesorResponse> buscarPorEspecialidad(String especialidad, String token) {

        log.info("Buscando profesores por especialidad {}", especialidad);

        return repo.findByEspecialidadContainingIgnoreCase(especialidad)
                .stream()
                .map(p -> mapToResponse(p, token))
                .toList();
    }

    public List<ProfesorResponse> buscarPorFacultad(Long idFacultad, String token) {

        log.info("Buscando profesores por facultad {}", idFacultad);

        return repo.findByIdFacultad(idFacultad)
                .stream()
                .map(p -> mapToResponse(p, token))
                .toList();
    }

    private ProfesorResponse mapToResponse(Profesor profesor, String token) {

        log.debug("Mapeando profesor {} a response", profesor.getId());

        var facultad = facultadClient.obtenerFacultad(profesor.getIdFacultad(), token);

        return ProfesorResponse.builder()
                .id(profesor.getId())
                .rutP(profesor.getRutP())
                .nombreP(profesor.getNombreP())
                .emailP(profesor.getEmailP())
                .especialidad(profesor.getEspecialidad())
                .facultad(facultad)
                .build();
    }
}