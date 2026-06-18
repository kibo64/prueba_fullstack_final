package com.example.ms_notas.service;

import com.example.ms_notas.client.EstudianteClient;
import com.example.ms_notas.client.EvaluacionClient;
import com.example.ms_notas.dto.EstudianteResponse;
import com.example.ms_notas.dto.EvaluacionResponse;
import com.example.ms_notas.dto.NotaDTO;
import com.example.ms_notas.dto.NotaResponse;
import com.example.ms_notas.model.Nota;
import com.example.ms_notas.repository.NotaRepository;
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
class NotaServiceTest {

    @Mock
    private NotaRepository repo;

    @Mock
    private EstudianteClient estudianteClient;

    @Mock
    private EvaluacionClient evaluacionClient;

    @InjectMocks
    private NotaService service;

    private final String token = "Bearer test";

    @Test
    void deberiaEliminarNota() {

        doNothing().when(repo).deleteById(1L);

        service.eliminar(1L);

        verify(repo).deleteById(1L);
    }

    @Test
    void deberiaRetornarCeroCuandoNoHayNotas() {

        when(repo.findByIdEstudiante(1L))
                .thenReturn(List.of());

        Double promedio =
                service.promedioPorEstudiante(1L);

        assertEquals(0.0, promedio);
    }

    @Test
    void deberiaCalcularPromedioCorrectamente() {

        List<Nota> notas = List.of(
                new Nota(1L, 5.0, 1L, 1L),
                new Nota(2L, 7.0, 1L, 2L)
        );

        when(repo.findByIdEstudiante(1L))
                .thenReturn(notas);

        Double promedio =
                service.promedioPorEstudiante(1L);

        assertEquals(6.0, promedio);
    }

    @Test
    void deberiaLanzarExcepcionCuandoNotaNoExiste() {

        when(repo.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> service.obtener(99L, token)
            );
        }

    @Test
    void deberiaLanzarExcepcionCuandoNotaNoExisteAlActualizar() {

        NotaDTO dto = new NotaDTO();

        when(repo.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> service.actualizar(99L, dto, token)
            );
        }

    @Test
    void deberiaLanzarExcepcionCuandoEstudianteNoExisteAlCrear() {

        NotaDTO dto = new NotaDTO();
        dto.setIdEstudiante(1L);
        dto.setIdEvaluacion(1L);

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
    void deberiaLanzarExcepcionCuandoEvaluacionNoExisteAlCrear() {

        NotaDTO dto = new NotaDTO();
        dto.setIdEstudiante(1L);
        dto.setIdEvaluacion(1L);

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(new EstudianteResponse());

        when(evaluacionClient.obtenerEvaluacion(1L, token))
                .thenThrow(new RuntimeException());

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> service.crear(dto, token)
                );

        assertEquals(
                "Evaluación no existe",
                ex.getMessage()
        );
    }

    @Test
    void deberiaLanzarExcepcionCuandoEstudianteNoExisteAlActualizar() {

        Nota existente =
                new Nota(1L, 6.0, 1L, 1L);

        NotaDTO dto = new NotaDTO();
        dto.setIdEstudiante(2L);
        dto.setIdEvaluacion(1L);

        when(repo.findById(1L))
                .thenReturn(Optional.of(existente));

        when(estudianteClient.obtenerEstudiante(2L, token))
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
    void deberiaLanzarExcepcionCuandoEvaluacionNoExisteAlActualizar() {

        Nota existente =
                new Nota(1L, 6.0, 1L, 1L);

        NotaDTO dto = new NotaDTO();
        dto.setIdEstudiante(1L);
        dto.setIdEvaluacion(2L);

        when(repo.findById(1L))
                .thenReturn(Optional.of(existente));

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(new EstudianteResponse());

        when(evaluacionClient.obtenerEvaluacion(2L, token))
                .thenThrow(new RuntimeException());

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> service.actualizar(1L, dto, token)
                );

        assertEquals(
                "Evaluación no existe",
                ex.getMessage()
        );
    }
    @Test
    void deberiaCrearNota() {

        NotaDTO dto = new NotaDTO();
        dto.setNota(6.5);
        dto.setIdEstudiante(1L);
        dto.setIdEvaluacion(1L);

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        EvaluacionResponse evaluacion = new EvaluacionResponse();
        evaluacion.setId(1L);

        Nota guardada =
                new Nota(1L, 6.5, 1L, 1L);

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(evaluacionClient.obtenerEvaluacion(1L, token))
                .thenReturn(evaluacion);

        when(repo.save(any(Nota.class)))
                .thenReturn(guardada);

        NotaResponse resultado =
                service.crear(dto, token);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }
    @Test
    void deberiaObtenerNota() {

        Nota nota =
                new Nota(1L, 6.5, 1L, 1L);

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        EvaluacionResponse evaluacion = new EvaluacionResponse();
        evaluacion.setId(1L);

        when(repo.findById(1L))
                .thenReturn(Optional.of(nota));

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(evaluacionClient.obtenerEvaluacion(1L, token))
                .thenReturn(evaluacion);

        NotaResponse resultado =
                service.obtener(1L, token);

        assertEquals(1L, resultado.getId());
    }

    @Test
    void deberiaActualizarNota() {

        Nota nota =
                new Nota(1L, 5.0, 1L, 1L);

        NotaDTO dto = new NotaDTO();
        dto.setNota(7.0);
        dto.setIdEstudiante(1L);
        dto.setIdEvaluacion(1L);

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        EvaluacionResponse evaluacion = new EvaluacionResponse();
        evaluacion.setId(1L);

        when(repo.findById(1L))
                .thenReturn(Optional.of(nota));

        when(repo.save(any(Nota.class)))
                .thenReturn(nota);

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(evaluacionClient.obtenerEvaluacion(1L, token))
                .thenReturn(evaluacion);

        NotaResponse resultado =
                service.actualizar(1L, dto, token);

        assertNotNull(resultado);
    }
    @Test
    void deberiaBuscarPorEstudiante() {

        Nota nota =
                new Nota(1L, 6.5, 1L, 1L);

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        EvaluacionResponse evaluacion = new EvaluacionResponse();
        evaluacion.setId(1L);

        when(repo.findByIdEstudiante(1L))
                .thenReturn(List.of(nota));

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(evaluacionClient.obtenerEvaluacion(1L, token))
                .thenReturn(evaluacion);

        List<NotaResponse> resultado =
                service.buscarPorEstudiante(1L, token);

        assertEquals(1, resultado.size());
    }

    @Test
    void deberiaBuscarPorEvaluacion() {

        Nota nota =
                new Nota(1L, 6.5, 1L, 1L);

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        EvaluacionResponse evaluacion = new EvaluacionResponse();
        evaluacion.setId(1L);

        when(repo.findByIdEvaluacion(1L))
                .thenReturn(List.of(nota));

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(evaluacionClient.obtenerEvaluacion(1L, token))
                .thenReturn(evaluacion);

        List<NotaResponse> resultado =
                service.buscarPorEvaluacion(1L, token);

        assertEquals(1, resultado.size());
    }

    @Test
    void deberiaListarNotas() {

        Nota nota =
                new Nota(1L, 6.5, 1L, 1L);

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        EvaluacionResponse evaluacion = new EvaluacionResponse();
        evaluacion.setId(1L);

        when(repo.findAll())
                .thenReturn(List.of(nota));

        when(estudianteClient.obtenerEstudiante(1L, token))
                .thenReturn(estudiante);

        when(evaluacionClient.obtenerEvaluacion(1L, token))
                .thenReturn(evaluacion);

        List<NotaResponse> resultado =
                service.listar(token);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
    }
}