package com.example.ms_profesores.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ms_profesores.client.FacultadClient;
import com.example.ms_profesores.dto.FacultadResponse;
import com.example.ms_profesores.dto.ProfesorDTO;
import com.example.ms_profesores.dto.ProfesorResponse;
import com.example.ms_profesores.model.Profesor;
import com.example.ms_profesores.repository.ProfesorRepository;

@ExtendWith(MockitoExtension.class)
class ProfesorServiceTest {

    @Mock
    private ProfesorRepository repo;

    @Mock
    private FacultadClient facultadClient;

    @InjectMocks
    private ProfesorService service;

    private Profesor profesor;
    private ProfesorDTO dto;

    private final String token = "Bearer test";

    @BeforeEach
    void setUp() {

        profesor = new Profesor(
                1L,
                "11111111-1",
                "Juan",
                "juan@gmail.com",
                "Informática",
                1L
        );

        dto = new ProfesorDTO();
        dto.setRutP("11111111-1");
        dto.setNombreP("Juan");
        dto.setEmailP("juan@gmail.com");
        dto.setEspecialidad("Informática");
        dto.setIdFacultad(1L);
    }

    @Test
    void testCrear() {

        FacultadResponse facultad = new FacultadResponse();
        facultad.setId(1L);
        facultad.setNombre("Ingeniería");
        facultad.setUbicacion("Campus Norte");

        when(facultadClient.obtenerFacultad(1L, token))
                .thenReturn(facultad);

        when(repo.save(any(Profesor.class)))
                .thenReturn(profesor);

        ProfesorResponse res = service.crear(dto, token);

        assertNotNull(res);
        assertEquals("Juan", res.getNombreP());

        verify(repo).save(any(Profesor.class));
    }

    @Test
    void testCrearFacultadNoExiste() {

        when(facultadClient.obtenerFacultad(1L, token))
                .thenReturn(null);

        assertThrows(
                RuntimeException.class,
                () -> service.crear(dto, token)
        );
    }

    @Test
    void testListar() {

        FacultadResponse facultad = new FacultadResponse();
        facultad.setId(1L);
        facultad.setNombre("Ingeniería");
        facultad.setUbicacion("Campus Norte");

        when(repo.findAll())
                .thenReturn(List.of(profesor));

        when(facultadClient.obtenerFacultad(1L, token))
                .thenReturn(facultad);

        List<ProfesorResponse> lista =
                service.listar(token);

        assertEquals(1, lista.size());
    }

    @Test
    void testObtener() {

        FacultadResponse facultad = new FacultadResponse();
        facultad.setId(1L);
        facultad.setNombre("Ingeniería");
        facultad.setUbicacion("Campus Norte");

        when(repo.findById(1L))
                .thenReturn(Optional.of(profesor));

        when(facultadClient.obtenerFacultad(1L, token))
                .thenReturn(facultad);

        ProfesorResponse res =
                service.obtener(1L, token);

        assertEquals("Juan", res.getNombreP());
    }

    @Test
    void testObtenerNoExiste() {

        when(repo.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                jakarta.persistence.EntityNotFoundException.class,
                () -> service.obtener(1L, token)
        );
    }


    @Test
    void testActualizar() {

        FacultadResponse facultad = new FacultadResponse();
        facultad.setId(1L);
        facultad.setNombre("Ingeniería");
        facultad.setUbicacion("Campus Norte");

        when(repo.findById(1L))
                .thenReturn(Optional.of(profesor));

        when(repo.save(any(Profesor.class)))
                .thenReturn(profesor);

        when(facultadClient.obtenerFacultad(1L, token))
                .thenReturn(facultad);

        ProfesorResponse res =
                service.actualizar(1L, dto, token);

        assertEquals("Juan", res.getNombreP());
    }

    @Test
    void testActualizarNoExiste() {

        when(repo.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                jakarta.persistence.EntityNotFoundException.class,
                () -> service.actualizar(1L, dto, token)
        );
    }

    @Test
    void testEliminar() {

        doNothing().when(repo).deleteById(1L);

        service.eliminar(1L);

        verify(repo).deleteById(1L);
    }

    @Test
    void testBuscarPorNombre() {

        FacultadResponse facultad = new FacultadResponse();
        facultad.setId(1L);
        facultad.setNombre("Ingeniería");
        facultad.setUbicacion("Campus Norte");

        when(repo.findByNombrePContainingIgnoreCase("Juan"))
                .thenReturn(List.of(profesor));

        when(facultadClient.obtenerFacultad(1L, token))
                .thenReturn(facultad);

        assertEquals(
                1,
                service.buscarPorNombre("Juan", token).size()
        );
    }

    @Test
    void testBuscarPorEspecialidad() {

        FacultadResponse facultad = new FacultadResponse();
        facultad.setId(1L);
        facultad.setNombre("Ingeniería");
        facultad.setUbicacion("Campus Norte");

        when(repo.findByEspecialidadContainingIgnoreCase("Informática"))
                .thenReturn(List.of(profesor));

        when(facultadClient.obtenerFacultad(1L, token))
                .thenReturn(facultad);

        assertEquals(
                1,
                service.buscarPorEspecialidad("Informática", token).size()
        );
    }

    @Test
    void testBuscarPorFacultad() {

        FacultadResponse facultad = new FacultadResponse();
        facultad.setId(1L);
        facultad.setNombre("Ingeniería");
        facultad.setUbicacion("Campus Norte");

        when(repo.findByIdFacultad(1L))
                .thenReturn(List.of(profesor));

        when(facultadClient.obtenerFacultad(1L, token))
                .thenReturn(facultad);

        assertEquals(
                1,
                service.buscarPorFacultad(1L, token).size()
        );
    }
}