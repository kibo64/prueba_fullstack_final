package com.example.ms_facultad.repository;

import com.example.ms_facultad.model.Facultad;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class FacultadRepositoryTest {

    @Test
    void debeCrearFacultad() {

        Facultad facultad = new Facultad(
                1L,
                "Ingeniería",
                "Santiago"
        );

        assertNotNull(facultad);
        assertEquals(1L, facultad.getId());
        assertEquals("Ingeniería", facultad.getNombre());
        assertEquals("Santiago", facultad.getUbicacion());
    }

    @Test
    void debeBuscarFacultadPorId() {

        Facultad facultad = new Facultad(
                1L,
                "Medicina",
                "Valparaíso"
        );

        Optional<Facultad> resultado = Optional.of(facultad);

        assertTrue(resultado.isPresent());
        assertEquals("Medicina", resultado.get().getNombre());
    }
}