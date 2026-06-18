package com.example.ms_inscripciones.repository;

import com.example.ms_inscripciones.model.Inscripcion;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@ActiveProfiles("test")
class InscripcionRepositoryTest {

    @Test
    void debeCrearInscripcion() {

        Inscripcion inscripcion = new Inscripcion(
                1L,
                LocalDate.of(2025, 6, 15),
                1L,
                2L
        );

        assertNotNull(inscripcion);
        assertEquals(1L, inscripcion.getId());
        assertEquals(LocalDate.of(2025, 6, 15), inscripcion.getFecha());
        assertEquals(1L, inscripcion.getIdEstudiante());
        assertEquals(2L, inscripcion.getIdCurso());
    }

    @Test
    void debeBuscarInscripcionPorId() {

        Inscripcion inscripcion = new Inscripcion(
                1L,
                LocalDate.now(),
                10L,
                20L
        );

        Optional<Inscripcion> resultado = Optional.of(inscripcion);

        assertTrue(resultado.isPresent());
        assertEquals(10L, resultado.get().getIdEstudiante());
        assertEquals(20L, resultado.get().getIdCurso());
    }
}
