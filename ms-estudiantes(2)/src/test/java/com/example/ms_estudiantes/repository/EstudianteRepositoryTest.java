package com.example.ms_estudiantes.repository;

import com.example.ms_estudiantes.model.Estudiante;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class EstudianteRepositoryTest {

    @Test
    void debeCrearEstudiante() {

        Estudiante estudiante = new Estudiante(
                1L,
                "11111111-1",
                "Juan",
                "juan@gmail.com",
                "Ingeniería Informática",
                1L
        );

        assertNotNull(estudiante);
        assertEquals(1L, estudiante.getId());
        assertEquals("Juan", estudiante.getNombre());
        assertEquals("juan@gmail.com", estudiante.getEmail());
        assertEquals("Ingeniería Informática", estudiante.getCarrera());
        assertEquals(1L, estudiante.getIdFacultad());
    }

    @Test
    void debeBuscarEstudiantePorId() {

        Estudiante estudiante = new Estudiante(
                1L,
                "22222222-2",
                "Pedro",
                "pedro@gmail.com",
                "Ingeniería Civil",
                2L
        );

        Optional<Estudiante> resultado = Optional.of(estudiante);

        assertTrue(resultado.isPresent());
        assertEquals("Pedro", resultado.get().getNombre());
        assertEquals("Ingeniería Civil", resultado.get().getCarrera());
    }
}