package com.example.ms_cursos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ms_cursos.client.FacultadClient;
import com.example.ms_cursos.client.ProfesorClient;
import com.example.ms_cursos.dto.CursoDTO;
import com.example.ms_cursos.dto.CursoResponse;
import com.example.ms_cursos.model.Curso;
import com.example.ms_cursos.repository.CursoRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CursoService {

    private final CursoRepository repo;
    private final ProfesorClient profesorClient;
    private final FacultadClient facultadClient;

    public CursoResponse crear(CursoDTO dto, String token) {

        log.info("Creando curso: {}", dto.getNombreC());

        var profesor = profesorClient.obtenerProfesor(dto.getIdProfesor(), token);
        var facultad = facultadClient.obtenerFacultad(dto.getIdFacultad(), token);

        if (profesor == null) {
            log.error("Profesor {} no existe", dto.getIdProfesor());
            throw new RuntimeException("Profesor no existe");
        }

        if (facultad == null) {
            log.error("Facultad {} no existe", dto.getIdFacultad());
            throw new RuntimeException("Facultad no existe");
        }

        Curso curso = repo.save(
                new Curso(
                        null,
                        dto.getNombreC(),
                        dto.getCantidadE(),
                        dto.getDescripcion(),
                        dto.getIdProfesor(),
                        dto.getIdFacultad()
                )
        );

        log.info("Curso creado con id {}", curso.getId());

        return mapToResponse(curso, token);
    }

    public List<CursoResponse> listar(String token) {

        log.info("Listando cursos");

        return repo.findAll()
                .stream()
                .map(c -> mapToResponse(c, token))
                .toList();
    }

    public CursoResponse obtener(Long id, String token) {

        log.info("Buscando curso con id {}", id);

        Curso curso = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado"));

        return mapToResponse(curso, token);
    }

    public CursoResponse actualizar(Long id, CursoDTO dto, String token) {

        log.info("Actualizando curso con id {}", id);

        Curso c = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado"));

        var profesor = profesorClient.obtenerProfesor(dto.getIdProfesor(), token);
        var facultad = facultadClient.obtenerFacultad(dto.getIdFacultad(), token);

        if (profesor == null) {
            log.error("Profesor {} no existe", dto.getIdProfesor());
            throw new RuntimeException("Profesor no existe");
        }

        if (facultad == null) {
            log.error("Facultad {} no existe", dto.getIdFacultad());
            throw new RuntimeException("Facultad no existe");
        }

        c.setNombreC(dto.getNombreC());
        c.setCantidadE(dto.getCantidadE());
        c.setDescripcion(dto.getDescripcion());
        c.setIdProfesor(dto.getIdProfesor());
        c.setIdFacultad(dto.getIdFacultad());

        log.info("Curso actualizado con id {}", id);

        return mapToResponse(repo.save(c), token);
    }

    public void eliminar(Long id) {

        log.info("Eliminando curso con id {}", id);

        repo.deleteById(id);
    }

    public List<CursoResponse> buscarPorNombre(String nombre, String token) {

        log.info("Buscando cursos por nombre {}", nombre);

        return repo.findByNombreCContainingIgnoreCase(nombre)
                .stream()
                .map(c -> mapToResponse(c, token))
                .toList();
    }

    public List<CursoResponse> buscarPorProfesor(Long idProfesor, String token) {

        log.info("Buscando cursos por profesor {}", idProfesor);

        return repo.findByIdProfesor(idProfesor)
                .stream()
                .map(c -> mapToResponse(c, token))
                .toList();
    }

    public List<CursoResponse> buscarPorFacultad(Long idFacultad, String token) {

        log.info("Buscando cursos por facultad {}", idFacultad);

        return repo.findByIdFacultad(idFacultad)
                .stream()
                .map(c -> mapToResponse(c, token))
                .toList();
    }

    private CursoResponse mapToResponse(Curso curso, String token) {

        log.debug("Mapeando curso {} a response", curso.getId());

        var profesor = profesorClient.obtenerProfesor(curso.getIdProfesor(), token);
        var facultad = facultadClient.obtenerFacultad(curso.getIdFacultad(), token);

        return CursoResponse.builder()
                .id(curso.getId())
                .nombreC(curso.getNombreC())
                .cantidadE(curso.getCantidadE())
                .descripcion(curso.getDescripcion())
                .profesor(profesor)
                .facultad(facultad)
                .build();
    }
}