package com.example.ms_historial_academico.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ms_historial_academico.client.CursoClient;
import com.example.ms_historial_academico.client.EstudianteClient;
import com.example.ms_historial_academico.dto.HistorialAcademicoDTO;
import com.example.ms_historial_academico.dto.HistorialAcademicoResponse;
import com.example.ms_historial_academico.model.HistorialAcademico;
import com.example.ms_historial_academico.repository.HistorialAcademicoRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class HistorialAcademicoService {

    private final HistorialAcademicoRepository repo;
    private final EstudianteClient estudianteClient;
    private final CursoClient cursoClient;

    public HistorialAcademicoResponse crear(HistorialAcademicoDTO dto, String token) {

        log.info("Creando historial académico para estudiante {} y curso {}",
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

        String estado = calcularEstado(dto.getPromedioFinal(), dto.getAsistenciaFinal());

        HistorialAcademico historial = repo.save(
                new HistorialAcademico(
                        null,
                        dto.getPromedioFinal(),
                        dto.getAsistenciaFinal(),
                        estado,
                        dto.getIdEstudiante(),
                        dto.getIdCurso()
                )
        );

        log.info("Historial académico creado con id {}", historial.getId());

        return mapToResponse(historial, token);
    }

    public List<HistorialAcademicoResponse> listar(String token) {

        log.info("Listando historiales académicos");

        return repo.findAll()
                .stream()
                .map(h -> mapToResponse(h, token))
                .toList();
    }

    public HistorialAcademicoResponse obtener(Long id, String token) {

        log.info("Buscando historial académico con id {}", id);

        HistorialAcademico historial = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Historial no encontrado"));

        return mapToResponse(historial, token);
    }

    public HistorialAcademicoResponse actualizar(Long id, HistorialAcademicoDTO dto, String token) {

        log.info("Actualizando historial académico con id {}", id);

        HistorialAcademico h = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Historial no encontrado"));

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

        h.setPromedioFinal(dto.getPromedioFinal());
        h.setAsistenciaFinal(dto.getAsistenciaFinal());
        h.setEstado(calcularEstado(dto.getPromedioFinal(), dto.getAsistenciaFinal()));
        h.setIdEstudiante(dto.getIdEstudiante());
        h.setIdCurso(dto.getIdCurso());

        log.info("Historial académico actualizado con id {}", id);

        return mapToResponse(repo.save(h), token);
    }

    public void eliminar(Long id) {

        log.info("Eliminando historial académico con id {}", id);

        repo.deleteById(id);
    }

    public List<HistorialAcademicoResponse> buscarPorEstudiante(Long idEstudiante, String token) {

        log.info("Buscando historial académico por estudiante {}", idEstudiante);

        return repo.findByIdEstudiante(idEstudiante)
                .stream()
                .map(h -> mapToResponse(h, token))
                .toList();
    }

    public List<HistorialAcademicoResponse> buscarPorCurso(Long idCurso, String token) {

        log.info("Buscando historial académico por curso {}", idCurso);

        return repo.findByIdCurso(idCurso)
                .stream()
                .map(h -> mapToResponse(h, token))
                .toList();
    }

    public List<HistorialAcademicoResponse> buscarPorEstado(String estado, String token) {

        log.info("Buscando historial académico por estado {}", estado);

        return repo.findByEstadoIgnoreCase(estado)
                .stream()
                .map(h -> mapToResponse(h, token))
                .toList();
    }

    private String calcularEstado(Double promedio, Double asistencia) {

        if (promedio >= 4.0 && asistencia >= 75.0) {
            return "APROBADO";
        }

        return "REPROBADO";
    }

    private HistorialAcademicoResponse mapToResponse(HistorialAcademico historial, String token) {

        log.debug("Mapeando historial académico {} a response", historial.getId());

        var estudiante = estudianteClient.obtenerEstudiante(historial.getIdEstudiante(), token);
        var curso = cursoClient.obtenerCurso(historial.getIdCurso(), token);

        return HistorialAcademicoResponse.builder()
                .id(historial.getId())
                .promedioFinal(historial.getPromedioFinal())
                .asistenciaFinal(historial.getAsistenciaFinal())
                .estado(historial.getEstado())
                .estudiante(estudiante)
                .curso(curso)
                .build();
    }
}