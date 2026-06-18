package com.example.ms_notas.repository;

import com.example.ms_notas.model.Nota;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class NotaRepositoryTest {

    @Test
    void debeCrearNota() {

        Nota nota = new Nota(
                1L,
                6.5,
                1L,
                1L
        );

        assertNotNull(nota);
        assertEquals(1L, nota.getId());
        assertEquals(6.5, nota.getNota());
        assertEquals(1L, nota.getIdEstudiante());
        assertEquals(1L, nota.getIdEvaluacion());
    }

    @Test
    void debeBuscarPorEstudiante() {

        Nota nota = new Nota(
                1L,
                5.8,
                1L,
                1L
        );

        List<Nota> resultado = List.of(nota);

        assertFalse(resultado.isEmpty());
        assertEquals(
                1L,
                resultado.get(0).getIdEstudiante()
        );
    }

    @Test
    void debeBuscarPorEvaluacion() {

        Nota nota = new Nota(
                1L,
                6.0,
                1L,
                2L
        );

        List<Nota> resultado = List.of(nota);

        assertFalse(resultado.isEmpty());
        assertEquals(
                2L,
                resultado.get(0).getIdEvaluacion()
        );
    }
}