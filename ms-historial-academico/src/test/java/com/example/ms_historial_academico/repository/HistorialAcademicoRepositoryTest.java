package com.example.ms_historial_academico.repository;

import com.example.ms_historial_academico.model.HistorialAcademico;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class HistorialAcademicoRepositoryTest {

    @Test
    void debeCrearHistorialAcademico() {

        HistorialAcademico historial =
                new HistorialAcademico(
                        1L,
                        5.8,
                        90.0,
                        "APROBADO",
                        1L,
                        1L
                );

        assertNotNull(historial);
        assertEquals(1L, historial.getId());
        assertEquals(5.8, historial.getPromedioFinal());
        assertEquals(90.0, historial.getAsistenciaFinal());
        assertEquals("APROBADO", historial.getEstado());
    }

    @Test
    void debeBuscarPorEstudiante() {

        HistorialAcademico historial =
                new HistorialAcademico(
                        1L,
                        5.8,
                        90.0,
                        "APROBADO",
                        1L,
                        1L
                );

        List<HistorialAcademico> resultado =
                List.of(historial);

        assertFalse(resultado.isEmpty());
        assertEquals(
                1L,
                resultado.get(0).getIdEstudiante()
        );
    }

    @Test
    void debeBuscarPorCurso() {

        HistorialAcademico historial =
                new HistorialAcademico(
                        1L,
                        5.8,
                        90.0,
                        "APROBADO",
                        1L,
                        1L
                );

        List<HistorialAcademico> resultado =
                List.of(historial);

        assertFalse(resultado.isEmpty());
        assertEquals(
                1L,
                resultado.get(0).getIdCurso()
        );
    }

    @Test
    void debeBuscarPorEstado() {

        HistorialAcademico historial =
                new HistorialAcademico(
                        1L,
                        5.8,
                        90.0,
                        "APROBADO",
                        1L,
                        1L
                );

        List<HistorialAcademico> resultado =
                List.of(historial);

        assertFalse(resultado.isEmpty());
        assertEquals(
                "APROBADO",
                resultado.get(0).getEstado()
        );
    }
}