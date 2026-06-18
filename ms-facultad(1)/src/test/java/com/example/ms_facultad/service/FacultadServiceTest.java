package com.example.ms_facultad.service;

import com.example.ms_facultad.dto.FacultadDTO;
import com.example.ms_facultad.model.Facultad;
import com.example.ms_facultad.repository.FacultadRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacultadServiceTest {

    @Mock
    private FacultadRepository repo;

    @InjectMocks
    private FacultadService service;

    @Test
    void deberiaRetornarFacultadCuandoExiste() {

        Facultad facultad =
                new Facultad(1L, "Ingeniería", "Campus Norte");

        when(repo.findById(1L))
                .thenReturn(Optional.of(facultad));

        Facultad resultado = service.obtener(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Ingeniería", resultado.getNombre());
        assertEquals("Campus Norte", resultado.getUbicacion());

        verify(repo).findById(1L);
    }

    @Test
    void deberiaLanzarExcepcionCuandoNoExiste() {

        when(repo.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> service.obtener(99L)
        );

        verify(repo).findById(99L);
    }

    @Test
    void deberiaListarFacultades() {

        List<Facultad> lista = List.of(
                new Facultad(1L, "Ingeniería", "Campus Norte")
        );

        when(repo.findAll()).thenReturn(lista);

        List<Facultad> resultado = service.listar();

        assertEquals(1, resultado.size());
        assertEquals("Ingeniería", resultado.get(0).getNombre());

        verify(repo).findAll();
    }

    @Test
    void deberiaCrearFacultad() {

        FacultadDTO dto = new FacultadDTO();
        dto.setNombre("Ingeniería");
        dto.setUbicacion("Campus Norte");

        Facultad guardada =
                new Facultad(1L, "Ingeniería", "Campus Norte");

        when(repo.save(any(Facultad.class)))
                .thenReturn(guardada);

        Facultad resultado = service.crear(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Ingeniería", resultado.getNombre());
        assertEquals("Campus Norte", resultado.getUbicacion());

        verify(repo).save(any(Facultad.class));
    }

    @Test
    void deberiaActualizarFacultad() {

        Facultad existente =
                new Facultad(1L, "Antigua", "Campus Viejo");

        FacultadDTO dto = new FacultadDTO();
        dto.setNombre("Nueva");
        dto.setUbicacion("Campus Nuevo");

        when(repo.findById(1L))
                .thenReturn(Optional.of(existente));

        when(repo.save(any(Facultad.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Facultad resultado = service.actualizar(1L, dto);

        assertEquals(1L, resultado.getId());
        assertEquals("Nueva", resultado.getNombre());
        assertEquals("Campus Nuevo", resultado.getUbicacion());

        verify(repo).findById(1L);
        verify(repo).save(existente);
    }

    @Test
    void deberiaLanzarExcepcionAlActualizarFacultadInexistente() {

        FacultadDTO dto = new FacultadDTO();
        dto.setNombre("Nueva");
        dto.setUbicacion("Campus Nuevo");

        when(repo.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> service.actualizar(99L, dto)
        );

        verify(repo).findById(99L);
        verify(repo, never()).save(any(Facultad.class));
    }

    @Test
    void deberiaEliminarFacultad() {

        doNothing().when(repo).deleteById(1L);

        service.eliminar(1L);

        verify(repo).deleteById(1L);
    }

    @Test
    void deberiaBuscarPorNombre() {

        List<Facultad> lista = List.of(
                new Facultad(1L, "Ingeniería", "Campus Norte")
        );

        when(repo.findByNombreContainingIgnoreCase("Ingeniería"))
                .thenReturn(lista);

        List<Facultad> resultado =
                service.buscarPorNombre("Ingeniería");

        assertEquals(1, resultado.size());
        assertEquals("Ingeniería", resultado.get(0).getNombre());

        verify(repo).findByNombreContainingIgnoreCase("Ingeniería");
    }

    @Test
    void deberiaBuscarPorUbicacion() {

        List<Facultad> lista = List.of(
                new Facultad(1L, "Ingeniería", "Campus Norte")
        );

        when(repo.findByUbicacionContainingIgnoreCase("Campus Norte"))
                .thenReturn(lista);

        List<Facultad> resultado =
                service.buscarPorUbicacion("Campus Norte");

        assertEquals(1, resultado.size());
        assertEquals("Campus Norte", resultado.get(0).getUbicacion());

        verify(repo).findByUbicacionContainingIgnoreCase("Campus Norte");
    }
}