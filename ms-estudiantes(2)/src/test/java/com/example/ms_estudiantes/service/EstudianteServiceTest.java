package com.example.ms_estudiantes.service;

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

import com.example.ms_estudiantes.client.FacultadClient;
import com.example.ms_estudiantes.dto.EstudianteDTO;
import com.example.ms_estudiantes.dto.EstudianteResponse;
import com.example.ms_estudiantes.dto.FacultadResponse;
import com.example.ms_estudiantes.model.Estudiante;
import com.example.ms_estudiantes.repository.EstudianteRepository;

@ExtendWith(MockitoExtension.class)
class EstudianteServiceTest {

    @Mock
    private EstudianteRepository repo;

    @Mock
    private FacultadClient facultadClient;

    @InjectMocks
    private EstudianteService service;

    private Estudiante estudiante;
    private EstudianteDTO dto;

    private final String token = "Bearer test";

    @BeforeEach
    void setUp() {

        estudiante = new Estudiante(
                1L,
                "11111111-1",
                "Juan",
                "juan@gmail.com",
                "Ingeniería",
                1L
        );

        dto = new EstudianteDTO();
        dto.setRut("11111111-1");
        dto.setNombre("Juan");
        dto.setEmail("juan@gmail.com");
        dto.setCarrera("Ingeniería");
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

        when(repo.save(any(Estudiante.class)))
                .thenReturn(estudiante);

        EstudianteResponse res = service.crear(dto, token);

        assertNotNull(res);
        assertEquals("Juan", res.getNombre());

        verify(repo).save(any(Estudiante.class));
    }

    @Test
    void testListar() {

        FacultadResponse facultad = new FacultadResponse();
        facultad.setId(1L);
        facultad.setNombre("Ingeniería");
        facultad.setUbicacion("Campus Norte");

        when(repo.findAll())
                .thenReturn(List.of(estudiante));

        when(facultadClient.obtenerFacultad(1L, token))
                .thenReturn(facultad);

        List<EstudianteResponse> lista = service.listar(token);

        assertEquals(1, lista.size());
    }

    @Test
    void testObtener() {

        FacultadResponse facultad = new FacultadResponse();
        facultad.setId(1L);
        facultad.setNombre("Ingeniería");
        facultad.setUbicacion("Campus Norte");

        when(repo.findById(1L))
                .thenReturn(Optional.of(estudiante));

        when(facultadClient.obtenerFacultad(1L, token))
                .thenReturn(facultad);

        EstudianteResponse res = service.obtener(1L, token);

        assertNotNull(res);
        assertEquals("Juan", res.getNombre());
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
                .thenReturn(Optional.of(estudiante));

        when(repo.save(any(Estudiante.class)))
                .thenReturn(estudiante);

        when(facultadClient.obtenerFacultad(1L, token))
                .thenReturn(facultad);

        EstudianteResponse res =
                service.actualizar(1L, dto, token);

        assertNotNull(res);
        assertEquals("Juan", res.getNombre());
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

        when(repo.findByNombreContainingIgnoreCase("Juan"))
                .thenReturn(List.of(estudiante));

        when(facultadClient.obtenerFacultad(1L, token))
                .thenReturn(facultad);

        List<EstudianteResponse> lista =
                service.buscarPorNombre("Juan", token);

        assertEquals(1, lista.size());
    }

    @Test
    void testBuscarPorCarrera() {

        FacultadResponse facultad = new FacultadResponse();
        facultad.setId(1L);
        facultad.setNombre("Ingeniería");
        facultad.setUbicacion("Campus Norte");

        when(repo.findByCarreraContainingIgnoreCase("Ingeniería"))
                .thenReturn(List.of(estudiante));

        when(facultadClient.obtenerFacultad(1L, token))
                .thenReturn(facultad);

        List<EstudianteResponse> lista =
                service.buscarPorCarrera("Ingeniería", token);

        assertEquals(1, lista.size());
    }

    @Test
    void testBuscarPorFacultad() {

        FacultadResponse facultad = new FacultadResponse();
        facultad.setId(1L);
        facultad.setNombre("Ingeniería");
        facultad.setUbicacion("Campus Norte");

        when(repo.findByIdFacultad(1L))
                .thenReturn(List.of(estudiante));

        when(facultadClient.obtenerFacultad(1L, token))
                .thenReturn(facultad);

        List<EstudianteResponse> lista =
                service.buscarPorFacultad(1L, token);

        assertEquals(1, lista.size());
    }

    @Test
    void testCrearFacultadNoExiste() {

        when(facultadClient.obtenerFacultad(1L, token))
                .thenReturn(null);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.crear(dto, token)
        );

        assertEquals("Facultad no existe", ex.getMessage());
    }
}