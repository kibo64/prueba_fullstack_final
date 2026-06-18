package com.example.ms_notas.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ms_notas.client.EstudianteClient;
import com.example.ms_notas.client.EvaluacionClient;
import com.example.ms_notas.dto.NotaDTO;
import com.example.ms_notas.dto.NotaResponse;
import com.example.ms_notas.model.Nota;
import com.example.ms_notas.repository.NotaRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotaService {

    private final NotaRepository repo;
    private final EstudianteClient estudianteClient;
    private final EvaluacionClient evaluacionClient;

    public NotaResponse crear(NotaDTO dto, String token) {

        log.info("Creando nota para estudiante {} y evaluación {}",
                dto.getIdEstudiante(), dto.getIdEvaluacion());

        try {
            estudianteClient.obtenerEstudiante(dto.getIdEstudiante(), token);
        } catch (Exception e) {
            log.error("Estudiante {} no existe", dto.getIdEstudiante());
            throw new RuntimeException("Estudiante no existe");
        }

        try {
            evaluacionClient.obtenerEvaluacion(dto.getIdEvaluacion(), token);
        } catch (Exception e) {
            log.error("Evaluación {} no existe", dto.getIdEvaluacion());
            throw new RuntimeException("Evaluación no existe");
        }

        Nota nota = repo.save(
                new Nota(
                        null,
                        dto.getNota(),
                        dto.getIdEstudiante(),
                        dto.getIdEvaluacion()
                )
        );

        log.info("Nota creada con id {}", nota.getId());

        return mapToResponse(nota, token);
    }

    public List<NotaResponse> listar(String token) {

        log.info("Listando notas");

        return repo.findAll()
                .stream()
                .map(n -> mapToResponse(n, token))
                .toList();
    }

    public NotaResponse obtener(Long id, String token) {

        log.info("Buscando nota con id {}", id);

        Nota nota = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nota no encontrada"));

        return mapToResponse(nota, token);
    }

    public NotaResponse actualizar(Long id, NotaDTO dto, String token) {

        log.info("Actualizando nota con id {}", id);

        Nota n = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nota no encontrada"));

        try {
            estudianteClient.obtenerEstudiante(dto.getIdEstudiante(), token);
        } catch (Exception e) {
            log.error("Estudiante {} no existe", dto.getIdEstudiante());
            throw new RuntimeException("Estudiante no existe");
        }

        try {
            evaluacionClient.obtenerEvaluacion(dto.getIdEvaluacion(), token);
        } catch (Exception e) {
            log.error("Evaluación {} no existe", dto.getIdEvaluacion());
            throw new RuntimeException("Evaluación no existe");
        }

        n.setNota(dto.getNota());
        n.setIdEstudiante(dto.getIdEstudiante());
        n.setIdEvaluacion(dto.getIdEvaluacion());

        log.info("Nota actualizada con id {}", id);

        return mapToResponse(repo.save(n), token);
    }

    public void eliminar(Long id) {

        log.info("Eliminando nota con id {}", id);

        repo.deleteById(id);
    }

    public List<NotaResponse> buscarPorEstudiante(Long idEstudiante, String token) {

        log.info("Buscando notas por estudiante {}", idEstudiante);

        return repo.findByIdEstudiante(idEstudiante)
                .stream()
                .map(n -> mapToResponse(n, token))
                .toList();
    }

    public List<NotaResponse> buscarPorEvaluacion(Long idEvaluacion, String token) {

        log.info("Buscando notas por evaluación {}", idEvaluacion);

        return repo.findByIdEvaluacion(idEvaluacion)
                .stream()
                .map(n -> mapToResponse(n, token))
                .toList();
    }

    public Double promedioPorEstudiante(Long idEstudiante) {

        log.info("Calculando promedio del estudiante {}", idEstudiante);

        List<Nota> notas = repo.findByIdEstudiante(idEstudiante);

        if (notas.isEmpty()) {
            return 0.0;
        }

        return notas.stream()
                .mapToDouble(Nota::getNota)
                .average()
                .orElse(0.0);
    }

    private NotaResponse mapToResponse(Nota nota, String token) {

        log.debug("Mapeando nota {} a response", nota.getId());

        var estudiante = estudianteClient.obtenerEstudiante(nota.getIdEstudiante(), token);
        var evaluacion = evaluacionClient.obtenerEvaluacion(nota.getIdEvaluacion(), token);

        return NotaResponse.builder()
                .id(nota.getId())
                .nota(nota.getNota())
                .estudiante(estudiante)
                .evaluacion(evaluacion)
                .build();
    }
}