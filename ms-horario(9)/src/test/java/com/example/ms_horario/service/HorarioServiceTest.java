package com.example.ms_horario.service;

import com.example.ms_horario.client.CursoClient;
import com.example.ms_horario.dto.CursoResponse;
import com.example.ms_horario.dto.HorarioDTO;
import com.example.ms_horario.dto.HorarioResponse;
import com.example.ms_horario.model.Horario;
import com.example.ms_horario.repository.HorarioRepository;

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
class HorarioServiceTest {

    @Mock
    private HorarioRepository repo;

    @Mock
    private CursoClient cursoClient;

    @InjectMocks
    private HorarioService service;

    private final String token = "Bearer test";

    @Test
    void deberiaCrearHorario() {

        HorarioDTO dto = new HorarioDTO();
        dto.setDia("Lunes");
        dto.setHoraInicio("08:00");
        dto.setHoraFin("10:00");
        dto.setSala("A101");
        dto.setIdCurso(1L);

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        when(repo.save(any(Horario.class)))
                .thenReturn(
                        new Horario(
                                1L,
                                "Lunes",
                                "08:00",
                                "10:00",
                                "A101",
                                1L
                        )
                );

        HorarioResponse response = service.crear(dto, token);

        assertNotNull(response);
        assertEquals("Lunes", response.getDia());
    }

    @Test
    void deberiaObtenerHorario() {

        Horario horario =
                new Horario(
                        1L,
                        "Lunes",
                        "08:00",
                        "10:00",
                        "A101",
                        1L
                );

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(repo.findById(1L))
                .thenReturn(Optional.of(horario));

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        HorarioResponse response = service.obtener(1L, token);

        assertNotNull(response);
        assertEquals("Lunes", response.getDia());
    }

    @Test
    void deberiaListarHorarios() {

        Horario horario =
                new Horario(
                        1L,
                        "Lunes",
                        "08:00",
                        "10:00",
                        "A101",
                        1L
                );

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(repo.findAll())
                .thenReturn(List.of(horario));

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        List<HorarioResponse> lista = service.listar(token);

        assertEquals(1, lista.size());
    }

    @Test
    void deberiaActualizarHorario() {

        Horario horario =
                new Horario(
                        1L,
                        "Lunes",
                        "08:00",
                        "10:00",
                        "A101",
                        1L
                );

        HorarioDTO dto = new HorarioDTO();
        dto.setDia("Martes");
        dto.setHoraInicio("09:00");
        dto.setHoraFin("11:00");
        dto.setSala("B202");
        dto.setIdCurso(1L);

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(repo.findById(1L))
                .thenReturn(Optional.of(horario));

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        when(repo.save(any(Horario.class)))
                .thenAnswer(i -> i.getArgument(0));

        HorarioResponse response =
                service.actualizar(1L, dto, token);

        assertEquals("Martes", response.getDia());
        assertEquals("09:00", response.getHoraInicio());
    }

    @Test
    void deberiaEliminarHorario() {

        service.eliminar(1L);

        verify(repo).deleteById(1L);
    }

    @Test
    void deberiaBuscarPorCurso() {

        Horario horario =
                new Horario(
                        1L,
                        "Lunes",
                        "08:00",
                        "10:00",
                        "A101",
                        1L
                );

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(repo.findByIdCurso(1L))
                .thenReturn(List.of(horario));

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        List<HorarioResponse> lista =
                service.buscarPorCurso(1L, token);

        assertEquals(1, lista.size());
    }

    @Test
    void deberiaBuscarPorDia() {

        Horario horario =
                new Horario(
                        1L,
                        "Lunes",
                        "08:00",
                        "10:00",
                        "A101",
                        1L
                );

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        when(repo.findByDiaIgnoreCase("Lunes"))
                .thenReturn(List.of(horario));

        when(cursoClient.obtenerCurso(1L, token))
                .thenReturn(curso);

        List<HorarioResponse> lista =
                service.buscarPorDia("Lunes", token);

        assertEquals(1, lista.size());
    }

    @Test
    void deberiaLanzarExcepcionCuandoCursoNoExisteAlCrear() {

        HorarioDTO dto = new HorarioDTO();
        dto.setIdCurso(1L);

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
    void deberiaLanzarExcepcionCuandoHorarioNoExiste() {

        when(repo.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> service.obtener(99L, token)
        );
    }

    @Test
    void deberiaLanzarExcepcionCuandoCursoNoExisteAlActualizar() {

        Horario horario =
                new Horario(
                        1L,
                        "Lunes",
                        "08:00",
                        "10:00",
                        "A101",
                        1L
                );

        HorarioDTO dto = new HorarioDTO();
        dto.setDia("Martes");
        dto.setHoraInicio("09:00");
        dto.setHoraFin("11:00");
        dto.setSala("B202");
        dto.setIdCurso(1L);

        when(repo.findById(1L))
                .thenReturn(Optional.of(horario));

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
    void deberiaLanzarExcepcionCuandoActualizarHorarioNoExiste() {

        HorarioDTO dto = new HorarioDTO();
        dto.setIdCurso(1L);

        when(repo.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> service.actualizar(99L, dto, token)
        );
    }
}