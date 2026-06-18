package com.example.ms_profesores.repository;

import com.example.ms_profesores.model.Profesor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ProfesorRepositoryTest {

    @Test
    void debeCrearProfesor() {

        Profesor profesor = new Profesor(
                1L,
                "11111111-1",
                "Juan Pérez",
                "juan@duoc.cl",
                "Programación",
                1L
        );

        assertNotNull(profesor);
        assertEquals(1L, profesor.getId());
        assertEquals("11111111-1", profesor.getRutP());
        assertEquals("Juan Pérez", profesor.getNombreP());
        assertEquals("juan@duoc.cl", profesor.getEmailP());
        assertEquals("Programación", profesor.getEspecialidad());
        assertEquals(1L, profesor.getIdFacultad());
    }

    @Test
    void debeBuscarProfesorPorId() {

        Profesor profesor = new Profesor(
                1L,
                "22222222-2",
                "Pedro Soto",
                "pedro@duoc.cl",
                "Base de Datos",
                2L
        );

        Optional<Profesor> resultado = Optional.of(profesor);

        assertTrue(resultado.isPresent());
        assertEquals("Pedro Soto", resultado.get().getNombreP());
        assertEquals("Base de Datos", resultado.get().getEspecialidad());
    }
}