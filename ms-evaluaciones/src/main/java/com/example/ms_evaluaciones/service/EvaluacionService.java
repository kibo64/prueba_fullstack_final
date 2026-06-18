package com.example.ms_evaluaciones.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ms_evaluaciones.client.CursoClient;
import com.example.ms_evaluaciones.dto.EvaluacionDTO;
import com.example.ms_evaluaciones.dto.EvaluacionResponse;
import com.example.ms_evaluaciones.model.Evaluacion;
import com.example.ms_evaluaciones.repository.EvaluacionRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EvaluacionService {

    private final EvaluacionRepository repo;
    private final CursoClient cursoClient;

    public EvaluacionResponse crear(EvaluacionDTO dto, String token) {

        log.info("Creando evaluación {} para curso {}",
                dto.getNombreE(), dto.getIdCurso());

        try {
            cursoClient.obtenerCurso(dto.getIdCurso(), token);
        } catch (Exception e) {
            log.error("Curso {} no existe", dto.getIdCurso());
            throw new RuntimeException("Curso no existe");
        }

        Evaluacion evaluacion = repo.save(
                new Evaluacion(
                        null,
                        dto.getNombreE(),
                        dto.getFecha(),
                        dto.getPonderacion(),
                        dto.getIdCurso()
                )
        );

        log.info("Evaluación creada con id {}", evaluacion.getId());

        return mapToResponse(evaluacion, token);
    }

    public List<EvaluacionResponse> listar(String token) {

        log.info("Listando evaluaciones");

        return repo.findAll()
                .stream()
                .map(e -> mapToResponse(e, token))
                .toList();
    }

    public EvaluacionResponse obtener(Long id, String token) {

        log.info("Buscando evaluación con id {}", id);

        Evaluacion evaluacion = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evaluación no encontrada"));

        return mapToResponse(evaluacion, token);
    }

    public EvaluacionResponse actualizar(Long id, EvaluacionDTO dto, String token) {

        log.info("Actualizando evaluación con id {}", id);

        Evaluacion e = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evaluación no encontrada"));

        try {
            cursoClient.obtenerCurso(dto.getIdCurso(), token);
        } catch (Exception ex) {
            log.error("Curso {} no existe", dto.getIdCurso());
            throw new RuntimeException("Curso no existe");
        }

        e.setNombreE(dto.getNombreE());
        e.setFecha(dto.getFecha());
        e.setPonderacion(dto.getPonderacion());
        e.setIdCurso(dto.getIdCurso());

        log.info("Evaluación actualizada con id {}", id);

        return mapToResponse(repo.save(e), token);
    }

    public void eliminar(Long id) {

        log.info("Eliminando evaluación con id {}", id);

        repo.deleteById(id);
    }

    public List<EvaluacionResponse> buscarPorNombre(String nombre, String token) {

        log.info("Buscando evaluaciones por nombre {}", nombre);

        return repo.findByNombreEContainingIgnoreCase(nombre)
                .stream()
                .map(e -> mapToResponse(e, token))
                .toList();
    }

    public List<EvaluacionResponse> buscarPorCurso(Long idCurso, String token) {

        log.info("Buscando evaluaciones por curso {}", idCurso);

        return repo.findByIdCurso(idCurso)
                .stream()
                .map(e -> mapToResponse(e, token))
                .toList();
    }

    private EvaluacionResponse mapToResponse(Evaluacion evaluacion, String token) {

        log.debug("Mapeando evaluación {} a response", evaluacion.getId());

        var curso = cursoClient.obtenerCurso(evaluacion.getIdCurso(), token);

        return EvaluacionResponse.builder()
                .id(evaluacion.getId())
                .nombreE(evaluacion.getNombreE())
                .fecha(evaluacion.getFecha())
                .ponderacion(evaluacion.getPonderacion())
                .curso(curso)
                .build();
    }
}