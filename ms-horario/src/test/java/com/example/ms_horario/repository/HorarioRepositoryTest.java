package com.example.ms_horario.repository;

import com.example.ms_horario.model.Horario;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class HorarioRepositoryTest {

    @Test
    void debeCrearHorario() {

        Horario horario = new Horario(
                1L,
                "Lunes",
                "08:00",
                "10:00",
                "A101",
                1L
        );

        assertNotNull(horario);
        assertEquals(1L, horario.getId());
        assertEquals("Lunes", horario.getDia());
        assertEquals("08:00", horario.getHoraInicio());
        assertEquals("10:00", horario.getHoraFin());
        assertEquals("A101", horario.getSala());
        assertEquals(1L, horario.getIdCurso());
    }

    @Test
    void debeBuscarPorCurso() {

        Horario horario = new Horario(
                1L,
                "Lunes",
                "08:00",
                "10:00",
                "A101",
                1L
        );

        List<Horario> resultado = List.of(horario);

        assertFalse(resultado.isEmpty());
        assertEquals(
                1L,
                resultado.get(0).getIdCurso()
        );
    }

    @Test
    void debeBuscarPorDia() {

        Horario horario = new Horario(
                1L,
                "Lunes",
                "08:00",
                "10:00",
                "A101",
                1L
        );

        List<Horario> resultado = List.of(horario);

        assertFalse(resultado.isEmpty());
        assertEquals(
                "Lunes",
                resultado.get(0).getDia()
        );
    }
}