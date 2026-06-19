package com.example.ms_asistencia.service;

import com.example.ms_asistencia.client.CursoClient;
import com.example.ms_asistencia.client.EstudianteClient;
import com.example.ms_asistencia.dto.AsistenciaDTO;
import com.example.ms_asistencia.dto.AsistenciaResponse;
import com.example.ms_asistencia.dto.CursoResponse;
import com.example.ms_asistencia.dto.EstudianteResponse;
import com.example.ms_asistencia.model.Asistencia;
import com.example.ms_asistencia.repository.AsistenciaRepository;
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
class AsistenciaServiceTest {

    @Mock
    private AsistenciaRepository repo;

    @Mock
    private EstudianteClient estudianteClient;

    @Mock
    private CursoClient cursoClient;

    @InjectMocks
    private AsistenciaService service;

    private final String token = "Bearer test";

    @Test
    void deberiaCrearAsistencia() {

        AsistenciaDTO dto = new AsistenciaDTO();
        dto.setFechaClase(LocalDate.now());
        dto.setAsistencia(true);
        dto.setIdEstudiante(1L);
        dto.setIdCurso(1L);

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        Asistencia asistencia =
                new Asistencia(
                        1L,
                        dto.getFechaClase(),
                        true,
                        1L,
                        1L
                );

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        when(repo.save(any(Asistencia.class)))
                .thenReturn(asistencia);

        AsistenciaResponse resultado =
                service.crear(dto, token);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void deberiaListarAsistencias() {

        Asistencia asistencia =
                new Asistencia(
                        1L,
                        LocalDate.now(),
                        true,
                        1L,
                        1L
                );

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(repo.findAll())
                .thenReturn(List.of(asistencia));

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        List<AsistenciaResponse> resultado =
                service.listar(token);

        assertEquals(1, resultado.size());
    }

    @Test
    void deberiaObtenerAsistenciaCuandoExiste() {

        Asistencia asistencia =
                new Asistencia(
                        1L,
                        LocalDate.now(),
                        true,
                        1L,
                        1L
                );

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(repo.findById(1L))
                .thenReturn(Optional.of(asistencia));

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        AsistenciaResponse resultado =
                service.obtener(1L, token);

        assertEquals(1L, resultado.getId());
    }

    @Test
    void deberiaLanzarExcepcionCuandoNoExisteAsistencia() {

        when(repo.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> service.obtener(1L, token)
        );
    }

    @Test
    void deberiaActualizarAsistencia() {

        Asistencia asistencia =
                new Asistencia(
                        1L,
                        LocalDate.now(),
                        true,
                        1L,
                        1L
                );

        AsistenciaDTO dto = new AsistenciaDTO();
        dto.setFechaClase(LocalDate.now());
        dto.setAsistencia(false);
        dto.setIdEstudiante(1L);
        dto.setIdCurso(1L);

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(repo.findById(1L))
                .thenReturn(Optional.of(asistencia));

        when(repo.save(any(Asistencia.class)))
                .thenReturn(asistencia);

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        AsistenciaResponse resultado =
                service.actualizar(1L, dto, token);

        assertNotNull(resultado);
    }

    @Test
    void deberiaEliminarAsistencia() {

        service.eliminar(1L);

        verify(repo).deleteById(1L);
    }

    @Test
    void deberiaBuscarPorEstudiante() {

        Asistencia asistencia =
                new Asistencia(
                        1L,
                        LocalDate.now(),
                        true,
                        1L,
                        1L
                );

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(repo.findByIdEstudiante(1L))
                .thenReturn(List.of(asistencia));

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        List<AsistenciaResponse> resultado =
                service.buscarPorEstudiante(1L, token);

        assertEquals(1, resultado.size());
    }

    @Test
    void deberiaBuscarPorCurso() {

        Asistencia asistencia =
                new Asistencia(
                        1L,
                        LocalDate.now(),
                        true,
                        1L,
                        1L
                );

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(repo.findByIdCurso(1L))
                .thenReturn(List.of(asistencia));

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        List<AsistenciaResponse> resultado =
                service.buscarPorCurso(1L, token);

        assertEquals(1, resultado.size());
    }

    @Test
    void deberiaCalcularPorcentajeAsistencia() {

        List<Asistencia> asistencias = List.of(
                new Asistencia(1L, LocalDate.now(), true, 1L, 1L),
                new Asistencia(2L, LocalDate.now(), true, 1L, 1L),
                new Asistencia(3L, LocalDate.now(), false, 1L, 1L)
        );

        when(repo.findByIdEstudianteAndIdCurso(1L, 1L))
                .thenReturn(asistencias);

        Double porcentaje =
                service.porcentajeAsistencia(1L, 1L);

        assertEquals(66.66666666666667, porcentaje);
    }

    @Test
    void deberiaRetornarCeroCuandoNoHayAsistencias() {

        when(repo.findByIdEstudianteAndIdCurso(1L, 1L))
                .thenReturn(List.of());

        Double porcentaje =
                service.porcentajeAsistencia(1L, 1L);

        assertEquals(0.0, porcentaje);
    }

    @Test
    void deberiaLanzarExcepcionCuandoEstudianteNoExisteAlCrear() {

        AsistenciaDTO dto = new AsistenciaDTO();
        dto.setIdEstudiante(1L);
        dto.setIdCurso(1L);

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenThrow(new RuntimeException());

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> service.crear(dto, token)
                );

        assertEquals(
                "Estudiante no existe",
                ex.getMessage()
        );
    }

    @Test
    void deberiaLanzarExcepcionCuandoCursoNoExisteAlCrear() {

        AsistenciaDTO dto = new AsistenciaDTO();
        dto.setIdEstudiante(1L);
        dto.setIdCurso(1L);

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(cursoClient.obtenerCurso(1L, token))
                .thenThrow(new RuntimeException());

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> service.crear(dto, token)
                );

        assertEquals(
                "Curso no existe",
                ex.getMessage()
        );
    }

    @Test
    void deberiaLanzarExcepcionCuandoEstudianteNoExisteAlActualizar() {

        Asistencia asistencia =
                new Asistencia(
                        1L,
                        LocalDate.now(),
                        true,
                        1L,
                        1L
                );

        AsistenciaDTO dto = new AsistenciaDTO();
        dto.setIdEstudiante(1L);
        dto.setIdCurso(1L);

        when(repo.findById(1L))
                .thenReturn(Optional.of(asistencia));

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenThrow(new RuntimeException());

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> service.actualizar(1L, dto, token)
                );

        assertEquals(
                "Estudiante no existe",
                ex.getMessage()
        );
    }

    @Test
    void deberiaLanzarExcepcionCuandoCursoNoExisteAlActualizar() {

        Asistencia asistencia =
                new Asistencia(
                        1L,
                        LocalDate.now(),
                        true,
                        1L,
                        1L
                );

        AsistenciaDTO dto = new AsistenciaDTO();
        dto.setIdEstudiante(1L);
        dto.setIdCurso(1L);

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        when(repo.findById(1L))
                .thenReturn(Optional.of(asistencia));

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(cursoClient.obtenerCurso(1L, token))
                .thenThrow(new RuntimeException());

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> service.actualizar(1L, dto, token)
                );

        assertEquals(
                "Curso no existe",
                ex.getMessage()
        );
    }


 
} 