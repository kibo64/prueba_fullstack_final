package com.example.ms_cursos.service;

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

import com.example.ms_cursos.client.FacultadClient;
import com.example.ms_cursos.client.ProfesorClient;
import com.example.ms_cursos.dto.CursoDTO;
import com.example.ms_cursos.dto.CursoResponse;
import com.example.ms_cursos.dto.FacultadResponse;
import com.example.ms_cursos.dto.ProfesorCursoResponse;
import com.example.ms_cursos.model.Curso;
import com.example.ms_cursos.repository.CursoRepository;


@ExtendWith(MockitoExtension.class)
class CursoServiceTest {

    @Mock
    private CursoRepository repo;

    @Mock
    private ProfesorClient profesorClient;

    @Mock
    private FacultadClient facultadClient;

    @InjectMocks
    private CursoService service;

    private Curso curso;
    private CursoDTO dto;

    private final String token = "Bearer test";

    @BeforeEach
    void setUp() {

        curso = new Curso(
                1L,
                "Programacion",
                40,
                "Curso de Java",
                1L,
                1L
        );

        dto = new CursoDTO();
        dto.setNombreC("Programacion");
        dto.setCantidadE(40);
        dto.setDescripcion("Curso de Java");
        dto.setIdProfesor(1L);
        dto.setIdFacultad(1L);
    }


    @Test
    void testCrear() {

        ProfesorCursoResponse profesor = new ProfesorCursoResponse();
        profesor.setId(1L);

        FacultadResponse facultad = new FacultadResponse();
        facultad.setId(1L);

        when(profesorClient.obtenerProfesor(1L, token))
                .thenReturn(profesor);

        when(facultadClient.obtenerFacultad(1L, token))
                .thenReturn(facultad);

        when(repo.save(any(Curso.class)))
                .thenReturn(curso);

        CursoResponse res = service.crear(dto, token);

        assertNotNull(res);
        assertEquals("Programacion", res.getNombreC());

        verify(repo).save(any(Curso.class));
    }

    @Test
    void testCrearProfesorNoExiste() {

        when(profesorClient.obtenerProfesor(1L, token))
                .thenReturn(null);

        FacultadResponse facultad = new FacultadResponse();
        facultad.setId(1L);

        when(facultadClient.obtenerFacultad(1L, token))
                .thenReturn(facultad);

        assertThrows(
                RuntimeException.class,
                () -> service.crear(dto, token)
        );
    }

    @Test
    void testCrearFacultadNoExiste() {

        ProfesorCursoResponse profesor = new ProfesorCursoResponse();
        profesor.setId(1L);

        when(profesorClient.obtenerProfesor(1L, token))
                .thenReturn(profesor);

        when(facultadClient.obtenerFacultad(1L, token))
                .thenReturn(null);

        assertThrows(
                RuntimeException.class,
                () -> service.crear(dto, token)
        );
    }

    @Test
    void testListar() {

        ProfesorCursoResponse profesor = new ProfesorCursoResponse();
        profesor.setId(1L);

        FacultadResponse facultad = new FacultadResponse();
        facultad.setId(1L);

        when(repo.findAll())
                .thenReturn(List.of(curso));

        when(profesorClient.obtenerProfesor(1L, token))
                .thenReturn(profesor);

        when(facultadClient.obtenerFacultad(1L, token))
                .thenReturn(facultad);

        List<CursoResponse> lista = service.listar(token);

        assertEquals(1, lista.size());
    }

    @Test
    void testObtener() {

        ProfesorCursoResponse profesor = new ProfesorCursoResponse();
        profesor.setId(1L);

        FacultadResponse facultad = new FacultadResponse();
        facultad.setId(1L);

        when(repo.findById(1L))
                .thenReturn(Optional.of(curso));

        when(profesorClient.obtenerProfesor(1L, token))
                .thenReturn(profesor);

        when(facultadClient.obtenerFacultad(1L, token))
                .thenReturn(facultad);

        CursoResponse res = service.obtener(1L, token);

        assertNotNull(res);
        assertEquals("Programacion", res.getNombreC());
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

        ProfesorCursoResponse profesor = new ProfesorCursoResponse();
        profesor.setId(1L);

        FacultadResponse facultad = new FacultadResponse();
        facultad.setId(1L);

        when(repo.findById(1L))
                .thenReturn(Optional.of(curso));

        when(repo.save(any(Curso.class)))
                .thenReturn(curso);

        when(profesorClient.obtenerProfesor(1L, token))
                .thenReturn(profesor);

        when(facultadClient.obtenerFacultad(1L, token))
                .thenReturn(facultad);

        CursoResponse res =
                service.actualizar(1L, dto, token);

        assertNotNull(res);
        assertEquals("Programacion", res.getNombreC());
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

        ProfesorCursoResponse profesor = new ProfesorCursoResponse();
        profesor.setId(1L);

        FacultadResponse facultad = new FacultadResponse();
        facultad.setId(1L);

        when(repo.findByNombreCContainingIgnoreCase("Programacion"))
                .thenReturn(List.of(curso));

        when(profesorClient.obtenerProfesor(1L, token))
                .thenReturn(profesor);

        when(facultadClient.obtenerFacultad(1L, token))
                .thenReturn(facultad);

        List<CursoResponse> lista =
                service.buscarPorNombre("Programacion", token);

        assertEquals(1, lista.size());
    }

    @Test
    void testBuscarPorProfesor() {

        ProfesorCursoResponse profesor = new ProfesorCursoResponse();
        profesor.setId(1L);

        FacultadResponse facultad = new FacultadResponse();
        facultad.setId(1L);

        when(repo.findByIdProfesor(1L))
                .thenReturn(List.of(curso));

        when(profesorClient.obtenerProfesor(1L, token))
                .thenReturn(profesor);

        when(facultadClient.obtenerFacultad(1L, token))
                .thenReturn(facultad);

        List<CursoResponse> lista =
                service.buscarPorProfesor(1L, token);

        assertEquals(1, lista.size());
    }

    @Test
    void testBuscarPorFacultad() {

        ProfesorCursoResponse profesor = new ProfesorCursoResponse();
        profesor.setId(1L);

        FacultadResponse facultad = new FacultadResponse();
        facultad.setId(1L);

        when(repo.findByIdFacultad(1L))
                .thenReturn(List.of(curso));

        when(profesorClient.obtenerProfesor(1L, token))
                .thenReturn(profesor);

        when(facultadClient.obtenerFacultad(1L, token))
                .thenReturn(facultad);

        List<CursoResponse> lista =
                service.buscarPorFacultad(1L, token);

        assertEquals(1, lista.size());
    }

    @Test
    void testActualizarProfesorNoExiste() {

        when(repo.findById(1L))
                .thenReturn(Optional.of(curso));

        when(profesorClient.obtenerProfesor(1L, token))
                .thenReturn(null);

        FacultadResponse facultad = new FacultadResponse();
        facultad.setId(1L);

        when(facultadClient.obtenerFacultad(1L, token))
                .thenReturn(facultad);

        assertThrows(
                RuntimeException.class,
                () -> service.actualizar(1L, dto, token)
        );
    }

    @Test
    void testActualizarFacultadNoExiste() {

        when(repo.findById(1L))
                .thenReturn(Optional.of(curso));

        ProfesorCursoResponse profesor = new ProfesorCursoResponse();
        profesor.setId(1L);

        when(profesorClient.obtenerProfesor(1L, token))
                .thenReturn(profesor);

        when(facultadClient.obtenerFacultad(1L, token))
                .thenReturn(null);

        assertThrows(
                RuntimeException.class,
                () -> service.actualizar(1L, dto, token)
        );
    }

}