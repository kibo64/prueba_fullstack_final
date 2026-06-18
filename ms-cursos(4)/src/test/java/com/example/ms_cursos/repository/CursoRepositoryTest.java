package com.example.ms_cursos.repository;

import com.example.ms_cursos.model.Curso;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CursoRepositoryTest {

    @Test
    void debeCrearCurso() {

        Curso curso = new Curso(
                1L,
                "Programación",
                40,
                "Curso de Java",
                1L,
                1L
        );

        assertNotNull(curso);
        assertEquals(1L, curso.getId());
        assertEquals("Programación", curso.getNombreC());
        assertEquals(40, curso.getCantidadE());
        assertEquals("Curso de Java", curso.getDescripcion());
        assertEquals(1L, curso.getIdProfesor());
        assertEquals(1L, curso.getIdFacultad());
    }

    @Test
    void debeBuscarCursoPorId() {

        Curso curso = new Curso(
                1L,
                "Base de Datos",
                35,
                "SQL",
                2L,
                3L
        );

        Optional<Curso> resultado = Optional.of(curso);

        assertTrue(resultado.isPresent());
        assertEquals("Base de Datos", resultado.get().getNombreC());
    }
}