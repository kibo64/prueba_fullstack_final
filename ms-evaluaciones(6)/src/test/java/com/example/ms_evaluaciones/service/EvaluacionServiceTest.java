package com.example.ms_evaluaciones.service;

import com.example.ms_evaluaciones.client.CursoClient;
import com.example.ms_evaluaciones.dto.CursoResponse;
import com.example.ms_evaluaciones.dto.EvaluacionDTO;
import com.example.ms_evaluaciones.dto.EvaluacionResponse;
import com.example.ms_evaluaciones.model.Evaluacion;
import com.example.ms_evaluaciones.repository.EvaluacionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvaluacionServiceTest {

    @Mock
    private EvaluacionRepository repo;

    @Mock
    private CursoClient cursoClient;

    @InjectMocks
    private EvaluacionService service;

    private final String token = "Bearer test";

    @Test
    void deberiaRetornarEvaluacionCuandoExiste() {

        Evaluacion evaluacion =
                new Evaluacion(
                        1L,
                        "Prueba 1",
                        LocalDate.now(),
                        30.0,
                        1L
                );

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);
        curso.setNombreC("Programación Backend");

        when(repo.findById(1L))
                .thenReturn(Optional.of(evaluacion));

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        EvaluacionResponse resultado =
                service.obtener(1L, token);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Prueba 1", resultado.getNombreE());

        verify(repo).findById(1L);
    }

    @Test
    void deberiaLanzarExcepcionCuandoEvaluacionNoExiste() {

        when(repo.findById(99L))
                .thenReturn(Optional.empty());

        EntityNotFoundException ex =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> service.obtener(99L, token)
                );

        assertEquals(
                "Evaluación no encontrada",
                ex.getMessage()
        );

        verify(repo).findById(99L);
    }

    @Test
    void deberiaListarEvaluaciones() {

        Evaluacion evaluacion =
                new Evaluacion(
                        1L,
                        "Prueba 1",
                        LocalDate.now(),
                        30.0,
                        1L
                );

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(repo.findAll())
                .thenReturn(List.of(evaluacion));

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        List<EvaluacionResponse> resultado =
                service.listar(token);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());

        verify(repo).findAll();
    }

    @Test
    void deberiaCrearEvaluacionCorrectamente() {

        EvaluacionDTO dto = new EvaluacionDTO();
        dto.setNombreE("Prueba 1");
        dto.setFecha(LocalDate.now());
        dto.setPonderacion(30.0);
        dto.setIdCurso(1L);

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        Evaluacion guardada =
                new Evaluacion(
                        1L,
                        dto.getNombreE(),
                        dto.getFecha(),
                        dto.getPonderacion(),
                        dto.getIdCurso()
                );

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        when(repo.save(any(Evaluacion.class)))
                .thenReturn(guardada);

        EvaluacionResponse resultado =
                service.crear(dto, token);

        assertNotNull(resultado);
        assertEquals("Prueba 1", resultado.getNombreE());

        verify(repo).save(any(Evaluacion.class));
    }

    @Test
    void deberiaActualizarEvaluacionCorrectamente() {

        Evaluacion existente =
                new Evaluacion(
                        1L,
                        "Antigua",
                        LocalDate.now(),
                        20.0,
                        1L
                );

        EvaluacionDTO dto = new EvaluacionDTO();
        dto.setNombreE("Nueva");
        dto.setFecha(LocalDate.now());
        dto.setPonderacion(40.0);
        dto.setIdCurso(1L);

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(repo.findById(1L))
                .thenReturn(Optional.of(existente));

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        when(repo.save(any(Evaluacion.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        EvaluacionResponse resultado =
                service.actualizar(1L, dto, token);

        assertEquals("Nueva", resultado.getNombreE());
        assertEquals(40.0, resultado.getPonderacion());

        verify(repo).findById(1L);
        verify(repo).save(existente);
    }

    @Test
    void deberiaEliminarEvaluacion() {

        doNothing().when(repo).deleteById(1L);

        service.eliminar(1L);

        verify(repo).deleteById(1L);
    }

    @Test
    void deberiaBuscarEvaluacionPorNombre() {

        Evaluacion evaluacion =
                new Evaluacion(
                        1L,
                        "Prueba 1",
                        LocalDate.now(),
                        30.0,
                        1L
                );

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(repo.findByNombreEContainingIgnoreCase("Prueba"))
                .thenReturn(List.of(evaluacion));

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        List<EvaluacionResponse> resultado =
                service.buscarPorNombre("Prueba", token);

        assertEquals(1, resultado.size());

        verify(repo).findByNombreEContainingIgnoreCase("Prueba");
    }

    @Test
    void deberiaBuscarEvaluacionPorCurso() {

        Evaluacion evaluacion =
                new Evaluacion(
                        1L,
                        "Prueba 1",
                        LocalDate.now(),
                        30.0,
                        1L
                );

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(repo.findByIdCurso(1L))
                .thenReturn(List.of(evaluacion));

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        List<EvaluacionResponse> resultado =
                service.buscarPorCurso(1L, token);

        assertEquals(1, resultado.size());

        verify(repo).findByIdCurso(1L);
    }

    @Test
    void deberiaLanzarExcepcionCuandoCursoNoExisteAlCrear() {

        EvaluacionDTO dto = new EvaluacionDTO();
        dto.setNombreE("Prueba");
        dto.setFecha(LocalDate.now());
        dto.setPonderacion(30.0);
        dto.setIdCurso(999L);

        when(cursoClient.obtenerCurso(999L, token))
                .thenThrow(new RuntimeException());

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> service.crear(dto, token)
                );

        assertEquals("Curso no existe", ex.getMessage());
    }

    @Test
    void deberiaLanzarExcepcionCuandoCursoNoExisteAlActualizar() {

        Evaluacion existente =
                new Evaluacion(
                        1L,
                        "Prueba",
                        LocalDate.now(),
                        30.0,
                        1L
                );

        EvaluacionDTO dto = new EvaluacionDTO();
        dto.setNombreE("Nueva");
        dto.setFecha(LocalDate.now());
        dto.setPonderacion(40.0);
        dto.setIdCurso(999L);

        when(repo.findById(1L))
                .thenReturn(Optional.of(existente));

        when(cursoClient.obtenerCurso(999L, token))
                .thenThrow(new RuntimeException());

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> service.actualizar(1L, dto, token)
                );

        assertEquals("Curso no existe", ex.getMessage());
    }
}