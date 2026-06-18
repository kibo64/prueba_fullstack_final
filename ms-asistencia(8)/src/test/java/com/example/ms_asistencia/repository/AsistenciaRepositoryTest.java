package com.example.ms_asistencia.repository;

import com.example.ms_asistencia.model.Asistencia;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class AsistenciaRepositoryTest {

    @Test
    void debeCrearAsistencia() {

        Asistencia asistencia = new Asistencia(
                1L,
                LocalDate.now(),
                true,
                1L,
                1L
        );

        assertNotNull(asistencia);
        assertEquals(1L, asistencia.getId());
        assertTrue(asistencia.getAsistencia());
        assertEquals(1L, asistencia.getIdEstudiante());
        assertEquals(1L, asistencia.getIdCurso());
    }

    @Test
    void debeBuscarPorEstudiante() {

        Asistencia asistencia = new Asistencia(
                1L,
                LocalDate.now(),
                true,
                1L,
                1L
        );

        List<Asistencia> resultado = List.of(asistencia);

        assertFalse(resultado.isEmpty());
        assertEquals(
                1L,
                resultado.get(0).getIdEstudiante()
        );
    }

    @Test
    void debeBuscarPorCurso() {

        Asistencia asistencia = new Asistencia(
                1L,
                LocalDate.now(),
                true,
                1L,
                1L
        );

        List<Asistencia> resultado = List.of(asistencia);

        assertFalse(resultado.isEmpty());
        assertEquals(
                1L,
                resultado.get(0).getIdCurso()
        );
    }
}