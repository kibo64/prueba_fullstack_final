package com.example.ms_inscripciones.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ms_inscripciones.client.CursoClient;
import com.example.ms_inscripciones.client.EstudianteClient;
import com.example.ms_inscripciones.dto.InscripcionDTO;
import com.example.ms_inscripciones.dto.InscripcionResponse;
import com.example.ms_inscripciones.model.Inscripcion;
import com.example.ms_inscripciones.repository.InscripcionRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InscripcionService {

    private final InscripcionRepository repo;
    private final EstudianteClient estudianteClient;
    private final CursoClient cursoClient;

    public InscripcionResponse crear(InscripcionDTO dto, String token) {

        log.info("Creando inscripción para estudiante {} en curso {}",
                dto.getIdEstudiante(), dto.getIdCurso());

        try {
            estudianteClient.obtenerEstudiante(dto.getIdEstudiante(), token);
        } catch (Exception e) {
            log.error("Estudiante {} no existe", dto.getIdEstudiante());
            throw new RuntimeException("Estudiante no existe");
        }

        try {
            cursoClient.obtenerCurso(dto.getIdCurso(), token);
        } catch (Exception e) {
            log.error("Curso {} no existe", dto.getIdCurso());
            throw new RuntimeException("Curso no existe");
        }

        if (repo.existsByIdEstudianteAndIdCurso(dto.getIdEstudiante(), dto.getIdCurso())) {
            log.error("El estudiante {} ya está inscrito en el curso {}",
                    dto.getIdEstudiante(), dto.getIdCurso());

            throw new RuntimeException("El estudiante ya está inscrito en este curso");
        }

        Inscripcion inscripcion = repo.save(
                new Inscripcion(
                        null,
                        dto.getFecha(),
                        dto.getIdEstudiante(),
                        dto.getIdCurso()
                )
        );

        log.info("Inscripción creada con id {}", inscripcion.getId());

        return mapToResponse(inscripcion, token);
    }

    public List<InscripcionResponse> listar(String token) {

        log.info("Listando inscripciones");

        return repo.findAll()
                .stream()
                .map(i -> mapToResponse(i, token))
                .toList();
    }

    public InscripcionResponse obtener(Long id, String token) {

        log.info("Buscando inscripción con id {}", id);

        Inscripcion inscripcion = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inscripción no encontrada"));

        return mapToResponse(inscripcion, token);
    }
    public InscripcionResponse actualizar(Long id, InscripcionDTO dto, String token) {

        log.info("Actualizando inscripción con id {}", id);

        Inscripcion i = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inscripción no encontrada"));

        try {
            estudianteClient.obtenerEstudiante(dto.getIdEstudiante(), token);
        } catch (Exception e) {
            log.error("Estudiante {} no existe", dto.getIdEstudiante());
            throw new RuntimeException("Estudiante no existe");
        }

        try {
            cursoClient.obtenerCurso(dto.getIdCurso(), token);
        } catch (Exception e) {
            log.error("Curso {} no existe", dto.getIdCurso());
            throw new RuntimeException("Curso no existe");
        }

        if (repo.existsByIdEstudianteAndIdCurso(dto.getIdEstudiante(), dto.getIdCurso())
                && (!i.getIdEstudiante().equals(dto.getIdEstudiante())
                || !i.getIdCurso().equals(dto.getIdCurso()))) {

            log.error("El estudiante {} ya está inscrito en el curso {}",
                    dto.getIdEstudiante(), dto.getIdCurso());

            throw new RuntimeException("El estudiante ya está inscrito en este curso");
        }

        i.setFecha(dto.getFecha());
        i.setIdEstudiante(dto.getIdEstudiante());
        i.setIdCurso(dto.getIdCurso());

        log.info("Inscripción actualizada con id {}", id);

        return mapToResponse(repo.save(i), token);
    }

    public void eliminar(Long id) {

        log.info("Eliminando inscripción con id {}", id);

        repo.deleteById(id);
    }

    public List<InscripcionResponse> buscarPorEstudiante(Long idEstudiante, String token) {

        log.info("Buscando inscripciones por estudiante {}", idEstudiante);

        return repo.findByIdEstudiante(idEstudiante)
                .stream()
                .map(i -> mapToResponse(i, token))
                .toList();
    }

    public List<InscripcionResponse> buscarPorCurso(Long idCurso, String token) {

        log.info("Buscando inscripciones por curso {}", idCurso);

        return repo.findByIdCurso(idCurso)
                .stream()
                .map(i -> mapToResponse(i, token))
                .toList();
    }

    private InscripcionResponse mapToResponse(Inscripcion inscripcion, String token) {

        log.debug("Mapeando inscripción {} a response", inscripcion.getId());

        var estudiante = estudianteClient.obtenerEstudiante(inscripcion.getIdEstudiante(), token);
        var curso = cursoClient.obtenerCurso(inscripcion.getIdCurso(), token);

        return InscripcionResponse.builder()
                .id(inscripcion.getId())
                .fecha(inscripcion.getFecha())
                .estudiante(estudiante)
                .curso(curso)
                .build();
    }
}