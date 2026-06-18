package com.example.ms_asistencia.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ms_asistencia.client.CursoClient;
import com.example.ms_asistencia.client.EstudianteClient;
import com.example.ms_asistencia.dto.AsistenciaDTO;
import com.example.ms_asistencia.dto.AsistenciaResponse;
import com.example.ms_asistencia.model.Asistencia;
import com.example.ms_asistencia.repository.AsistenciaRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsistenciaService {

    private final AsistenciaRepository repo;
    private final EstudianteClient estudianteClient;
    private final CursoClient cursoClient;

    public AsistenciaResponse crear(AsistenciaDTO dto, String token) {

        log.info("Creando asistencia para estudiante {} y curso {}",
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

        Asistencia asistencia = repo.save(
                new Asistencia(
                        null,
                        dto.getFechaClase(),
                        dto.getAsistencia(),
                        dto.getIdEstudiante(),
                        dto.getIdCurso()
                )
        );

        log.info("Asistencia creada con id {}", asistencia.getId());

        return mapToResponse(asistencia, token);
    }

    public List<AsistenciaResponse> listar(String token) {

        log.info("Listando asistencias");

        return repo.findAll()
                .stream()
                .map(a -> mapToResponse(a, token))
                .toList();
    }

    public AsistenciaResponse obtener(Long id, String token) {

        log.info("Buscando asistencia con id {}", id);

        Asistencia asistencia = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Asistencia no encontrada"));

        return mapToResponse(asistencia, token);
    }

    public AsistenciaResponse actualizar(Long id, AsistenciaDTO dto, String token) {

        log.info("Actualizando asistencia con id {}", id);

        Asistencia a = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Asistencia no encontrada"));

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

        a.setFechaClase(dto.getFechaClase());
        a.setAsistencia(dto.getAsistencia());
        a.setIdEstudiante(dto.getIdEstudiante());
        a.setIdCurso(dto.getIdCurso());

        log.info("Asistencia actualizada con id {}", id);

        return mapToResponse(repo.save(a), token);
    }

    public void eliminar(Long id) {

        log.info("Eliminando asistencia con id {}", id);

        repo.deleteById(id);
    }

    public List<AsistenciaResponse> buscarPorEstudiante(Long idEstudiante, String token) {

        log.info("Buscando asistencias por estudiante {}", idEstudiante);

        return repo.findByIdEstudiante(idEstudiante)
                .stream()
                .map(a -> mapToResponse(a, token))
                .toList();
    }

    public List<AsistenciaResponse> buscarPorCurso(Long idCurso, String token) {

        log.info("Buscando asistencias por curso {}", idCurso);

        return repo.findByIdCurso(idCurso)
                .stream()
                .map(a -> mapToResponse(a, token))
                .toList();
    }

    public Double porcentajeAsistencia(Long idEstudiante, Long idCurso) {

        log.info("Calculando porcentaje asistencia estudiante {} curso {}",
                idEstudiante, idCurso);

        List<Asistencia> asistencias =
                repo.findByIdEstudianteAndIdCurso(idEstudiante, idCurso);

        if (asistencias.isEmpty()) {
            return 0.0;
        }

        long presentes = asistencias.stream()
                .filter(Asistencia::getAsistencia)
                .count();

        return (presentes * 100.0) / asistencias.size();
    }

    private AsistenciaResponse mapToResponse(Asistencia asistencia, String token) {

        log.debug("Mapeando asistencia {} a response", asistencia.getId());

        var estudiante = estudianteClient.obtenerEstudiante(asistencia.getIdEstudiante(), token);
        var curso = cursoClient.obtenerCurso(asistencia.getIdCurso(), token);

        return AsistenciaResponse.builder()
                .id(asistencia.getId())
                .fechaClase(asistencia.getFechaClase())
                .asistencia(asistencia.getAsistencia())
                .estudiante(estudiante)
                .curso(curso)
                .build();
    }
}