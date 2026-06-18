package com.example.ms_inscripciones.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ms_inscripciones.client.CursoClient;
import com.example.ms_inscripciones.client.EstudianteClient;
import com.example.ms_inscripciones.dto.CursoResponse;
import com.example.ms_inscripciones.dto.EstudianteResponse;
import com.example.ms_inscripciones.dto.InscripcionDTO;
import com.example.ms_inscripciones.dto.InscripcionResponse;
import com.example.ms_inscripciones.model.Inscripcion;
import com.example.ms_inscripciones.repository.InscripcionRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class InscripcionServiceTest {

    @Mock
    private InscripcionRepository repo;

    @Mock
    private EstudianteClient estudianteClient;

    @Mock
    private CursoClient cursoClient;

    @InjectMocks
    private InscripcionService service;

    private Inscripcion inscripcion;
    private InscripcionDTO dto;

    private final String token = "Bearer test";

    @BeforeEach
    void setUp() {

        inscripcion = new Inscripcion(
                1L,
                LocalDate.now(),
                1L,
                1L
        );

        dto = new InscripcionDTO();
        dto.setFecha(LocalDate.now());
        dto.setIdEstudiante(1L);
        dto.setIdCurso(1L);
    }

    @Test
    void testCrear() {

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        when(repo.existsByIdEstudianteAndIdCurso(1L,1L))
                .thenReturn(false);

        when(repo.save(any(Inscripcion.class)))
                .thenReturn(inscripcion);

        InscripcionResponse res =
                service.crear(dto, token);

        assertNotNull(res);
        verify(repo).save(any(Inscripcion.class));
    }

    @Test
    void testCrearEstudianteNoExiste() {

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenThrow(new RuntimeException());

        assertThrows(
                RuntimeException.class,
                () -> service.crear(dto, token)
        );
    }

    @Test
    void testCrearCursoNoExiste() {

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(cursoClient.obtenerCurso(1L, token))
                .thenThrow(new RuntimeException());

        assertThrows(
                RuntimeException.class,
                () -> service.crear(dto, token)
        );
    }

    @Test
    void testCrearYaInscrito() {

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        when(repo.existsByIdEstudianteAndIdCurso(1L,1L))
                .thenReturn(true);

        assertThrows(
                RuntimeException.class,
                () -> service.crear(dto, token)
        );
    }

    @Test
    void testListar() {

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(repo.findAll())
                .thenReturn(List.of(inscripcion));

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        List<InscripcionResponse> lista =
                service.listar(token);

        assertEquals(1, lista.size());
    }

    @Test
    void testObtener() {

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(repo.findById(1L))
                .thenReturn(Optional.of(inscripcion));

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        InscripcionResponse res =
                service.obtener(1L, token);

        assertNotNull(res);
    }

    @Test
    void testObtenerNoExiste() {

        when(repo.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> service.obtener(1L, token)
        );
    }

    @Test
    void testActualizar() {

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(repo.findById(1L))
                .thenReturn(Optional.of(inscripcion));

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        when(repo.existsByIdEstudianteAndIdCurso(1L,1L))
                .thenReturn(false);

        when(repo.save(any(Inscripcion.class)))
                .thenReturn(inscripcion);

        InscripcionResponse res =
                service.actualizar(1L, dto, token);

        assertNotNull(res);
    }

    @Test
    void testActualizarNoExiste() {

        when(repo.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> service.actualizar(1L, dto, token)
        );
    }

    @Test
    void testEliminar() {

        doNothing().when(repo).deleteById(1L);

        service.eliminar(1L);

        verify(repo).deleteById(1L);
    }


    @Test
    void testBuscarPorEstudiante() {

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(repo.findByIdEstudiante(1L))
                .thenReturn(List.of(inscripcion));

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        List<InscripcionResponse> lista =
                service.buscarPorEstudiante(1L, token);

        assertEquals(1, lista.size());
    }

    @Test
    void testBuscarPorCurso() {

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(repo.findByIdCurso(1L))
                .thenReturn(List.of(inscripcion));

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        List<InscripcionResponse> lista =
                service.buscarPorCurso(1L, token);

        assertEquals(1, lista.size());
    }

    @Test
    void testActualizarYaInscrito() {

        when(repo.findById(1L))
                .thenReturn(Optional.of(inscripcion));
        dto.setIdEstudiante(2L);
        dto.setIdCurso(2L);

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(2L);

        CursoResponse curso = new CursoResponse();
        curso.setId(2L);

        when(estudianteClient.obtenerEstudiante(2L, token))
                .thenReturn(estudiante);

        when(cursoClient.obtenerCurso(2L, token))
                .thenReturn(curso);

        when(repo.existsByIdEstudianteAndIdCurso(2L, 2L))
                .thenReturn(true);

        assertThrows(
                RuntimeException.class,
                () -> service.actualizar(1L, dto, token)
        );
    }

    @Test
    void testActualizarEstudianteNoExiste() {

        when(repo.findById(1L))
                .thenReturn(Optional.of(inscripcion));

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenThrow(new RuntimeException());

        assertThrows(
                RuntimeException.class,
                () -> service.actualizar(1L, dto, token)
        );
    }

    @Test
    void testActualizarCursoNoExiste() {

        when(repo.findById(1L))
                .thenReturn(Optional.of(inscripcion));

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(cursoClient.obtenerCurso(1L, token))
                .thenThrow(new RuntimeException());

        assertThrows(
                RuntimeException.class,
                () -> service.actualizar(1L, dto, token)
        );
    }

}