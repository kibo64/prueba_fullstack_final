package com.example.ms_historial_academico.service;

import com.example.ms_historial_academico.client.CursoClient;
import com.example.ms_historial_academico.client.EstudianteClient;
import com.example.ms_historial_academico.dto.CursoResponse;
import com.example.ms_historial_academico.dto.EstudianteResponse;
import com.example.ms_historial_academico.dto.HistorialAcademicoDTO;
import com.example.ms_historial_academico.dto.HistorialAcademicoResponse;
import com.example.ms_historial_academico.model.HistorialAcademico;
import com.example.ms_historial_academico.repository.HistorialAcademicoRepository;

import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistorialAcademicoServiceTest {

    @Mock
    private HistorialAcademicoRepository repo;

    @Mock
    private EstudianteClient estudianteClient;

    @Mock
    private CursoClient cursoClient;

    @InjectMocks
    private HistorialAcademicoService service;

    private final String token = "Bearer test";

    @Test
    void deberiaCrearHistorial() {

        HistorialAcademicoDTO dto = new HistorialAcademicoDTO();
        dto.setPromedioFinal(5.5);
        dto.setAsistenciaFinal(90.0);
        dto.setIdEstudiante(1L);
        dto.setIdCurso(1L);

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        when(repo.save(any(HistorialAcademico.class)))
                .thenReturn(
                        new HistorialAcademico(
                                1L,
                                5.5,
                                90.0,
                                "APROBADO",
                                1L,
                                1L
                        )
                );

        HistorialAcademicoResponse response =
                service.crear(dto, token);

        assertNotNull(response);
        assertEquals("APROBADO", response.getEstado());
    }

    @Test
    void deberiaObtenerHistorial() {

        HistorialAcademico historial =
                new HistorialAcademico(
                        1L,
                        5.5,
                        90.0,
                        "APROBADO",
                        1L,
                        1L
                );

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(repo.findById(1L))
                .thenReturn(Optional.of(historial));

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        HistorialAcademicoResponse response =
                service.obtener(1L, token);

        assertNotNull(response);
        assertEquals("APROBADO", response.getEstado());
    }

    @Test
    void deberiaListarHistoriales() {

        HistorialAcademico historial =
                new HistorialAcademico(
                        1L,
                        5.5,
                        90.0,
                        "APROBADO",
                        1L,
                        1L
                );

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(repo.findAll())
                .thenReturn(List.of(historial));

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        List<HistorialAcademicoResponse> lista =
                service.listar(token);

        assertEquals(1, lista.size());
    }

    @Test
    void deberiaActualizarHistorial() {

        HistorialAcademico historial =
                new HistorialAcademico(
                        1L,
                        5.5,
                        90.0,
                        "APROBADO",
                        1L,
                        1L
                );

        HistorialAcademicoDTO dto = new HistorialAcademicoDTO();
        dto.setPromedioFinal(3.5);
        dto.setAsistenciaFinal(60.0);
        dto.setIdEstudiante(1L);
        dto.setIdCurso(1L);

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(repo.findById(1L))
                .thenReturn(Optional.of(historial));

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        when(repo.save(any(HistorialAcademico.class)))
                .thenAnswer(i -> i.getArgument(0));

        HistorialAcademicoResponse response =
                service.actualizar(1L, dto, token);

        assertEquals("REPROBADO", response.getEstado());
    }

    @Test
    void deberiaEliminarHistorial() {

        service.eliminar(1L);

        verify(repo).deleteById(1L);
    }

    @Test
    void deberiaBuscarPorEstudiante() {

        HistorialAcademico historial =
                new HistorialAcademico(
                        1L,
                        5.5,
                        90.0,
                        "APROBADO",
                        1L,
                        1L
                );

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(repo.findByIdEstudiante(1L))
                .thenReturn(List.of(historial));

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        List<HistorialAcademicoResponse> lista =
                service.buscarPorEstudiante(1L, token);

        assertEquals(1, lista.size());
    }

    @Test
    void deberiaBuscarPorCurso() {

        HistorialAcademico historial =
                new HistorialAcademico(
                        1L,
                        5.5,
                        90.0,
                        "APROBADO",
                        1L,
                        1L
                );

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(repo.findByIdCurso(1L))
                .thenReturn(List.of(historial));

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        List<HistorialAcademicoResponse> lista =
                service.buscarPorCurso(1L, token);

        assertEquals(1, lista.size());
    }

    @Test
    void deberiaBuscarPorEstado() {

        HistorialAcademico historial =
                new HistorialAcademico(
                        1L,
                        5.5,
                        90.0,
                        "APROBADO",
                        1L,
                        1L
                );

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(repo.findByEstadoIgnoreCase("APROBADO"))
                .thenReturn(List.of(historial));

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        List<HistorialAcademicoResponse> lista =
                service.buscarPorEstado("APROBADO", token);

        assertEquals(1, lista.size());
    }

    @Test
    void deberiaLanzarExcepcionCuandoEstudianteNoExisteAlCrear() {

        HistorialAcademicoDTO dto = new HistorialAcademicoDTO();
        dto.setIdEstudiante(1L);

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenThrow(new RuntimeException());

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> service.crear(dto, token)
                );

        assertEquals("Estudiante no existe", ex.getMessage());
    }

    @Test
    void deberiaLanzarExcepcionCuandoCursoNoExisteAlCrear() {

        HistorialAcademicoDTO dto = new HistorialAcademicoDTO();
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

        assertEquals("Curso no existe", ex.getMessage());
    }

    @Test
    void deberiaLanzarExcepcionCuandoHistorialNoExiste() {

        when(repo.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> service.obtener(99L, token)
        );
    }

    @Test
    void deberiaLanzarExcepcionCuandoCursoNoExisteAlActualizar() {

        HistorialAcademico historial =
                new HistorialAcademico(
                        1L,
                        5.5,
                        90.0,
                        "APROBADO",
                        1L,
                        1L
                );

        HistorialAcademicoDTO dto = new HistorialAcademicoDTO();
        dto.setPromedioFinal(5.0);
        dto.setAsistenciaFinal(80.0);
        dto.setIdEstudiante(1L);
        dto.setIdCurso(1L);

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        when(repo.findById(1L))
                .thenReturn(Optional.of(historial));

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(cursoClient.obtenerCurso(1L, token))
                .thenThrow(new RuntimeException());

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> service.actualizar(1L, dto, token)
                );

        assertEquals("Curso no existe", ex.getMessage());
    }

    @Test
    void deberiaLanzarExcepcionCuandoEstudianteNoExisteAlActualizar() {

        HistorialAcademico historial =
                new HistorialAcademico(
                        1L,
                        5.5,
                        90.0,
                        "APROBADO",
                        1L,
                        1L
                );

        HistorialAcademicoDTO dto = new HistorialAcademicoDTO();
        dto.setPromedioFinal(5.0);
        dto.setAsistenciaFinal(80.0);
        dto.setIdEstudiante(1L);
        dto.setIdCurso(1L);

        when(repo.findById(1L))
                .thenReturn(Optional.of(historial));

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenThrow(new RuntimeException());

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> service.actualizar(1L, dto, token)
                );

        assertEquals("Estudiante no existe", ex.getMessage());
    }

    @Test
    void deberiaCrearHistorialReprobado() {

        HistorialAcademicoDTO dto = new HistorialAcademicoDTO();
        dto.setPromedioFinal(3.0);
        dto.setAsistenciaFinal(60.0);
        dto.setIdEstudiante(1L);
        dto.setIdCurso(1L);

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        when(repo.save(any(HistorialAcademico.class)))
                .thenReturn(
                        new HistorialAcademico(
                                1L,
                                3.0,
                                60.0,
                                "REPROBADO",
                                1L,
                                1L
                        )
                );

        HistorialAcademicoResponse response =
                service.crear(dto, token);

        assertEquals("REPROBADO", response.getEstado());
    }

    @Test
    void deberiaActualizarHistorialAprobado() {

        HistorialAcademico historial =
                new HistorialAcademico(
                        1L,
                        3.0,
                        60.0,
                        "REPROBADO",
                        1L,
                        1L
                );

        HistorialAcademicoDTO dto = new HistorialAcademicoDTO();
        dto.setPromedioFinal(5.5);
        dto.setAsistenciaFinal(90.0);
        dto.setIdEstudiante(1L);
        dto.setIdCurso(1L);

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(repo.findById(1L))
                .thenReturn(Optional.of(historial));

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        when(repo.save(any(HistorialAcademico.class)))
                .thenAnswer(i -> i.getArgument(0));

        HistorialAcademicoResponse response =
                service.actualizar(1L, dto, token);

        assertEquals("APROBADO", response.getEstado());
    }
}