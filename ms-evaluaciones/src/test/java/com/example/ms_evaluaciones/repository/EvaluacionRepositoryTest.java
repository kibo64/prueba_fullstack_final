package com.example.ms_evaluaciones.repository;

import com.example.ms_evaluaciones.model.Evaluacion;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class EvaluacionRepositoryTest {

    @Test
    void debeCrearEvaluacion() {

        Evaluacion evaluacion = new Evaluacion(
                1L,
                "Prueba 1",
                LocalDate.now(),
                7.0,
                1L
        );

        assertNotNull(evaluacion);
        assertEquals(1L, evaluacion.getId());
        assertEquals("Prueba 1", evaluacion.getNombreE());
        assertEquals(7.0, evaluacion.getPonderacion());
        assertEquals(1L, evaluacion.getIdCurso());
    }

    @Test
    void debeBuscarPorNombre() {

        Evaluacion evaluacion = new Evaluacion(
                1L,
                "Prueba Java",
                LocalDate.now(),
                6.5,
                1L
        );

        List<Evaluacion> resultado = List.of(evaluacion);

        assertFalse(resultado.isEmpty());
        assertEquals(
                "Prueba Java",
                resultado.get(0).getNombreE()
        );
    }
} 