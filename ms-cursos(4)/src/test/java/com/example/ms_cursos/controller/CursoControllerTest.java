package com.example.ms_cursos.controller;

import com.example.ms_cursos.dto.*;
import com.example.ms_cursos.security.JwtFilter;
import com.example.ms_cursos.security.JwtUtil;
import com.example.ms_cursos.service.CursoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CursoController.class)
@AutoConfigureMockMvc(addFilters = false)
class CursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private CursoService service;

    @MockitoBean
    private JwtFilter jwtFilter;

    @MockitoBean
    private JwtUtil jwtUtil;

    private CursoResponse curso;
    private CursoDTO dto;

    @BeforeEach
    void setUp() {

        ProfesorCursoResponse profesor = new ProfesorCursoResponse();
        profesor.setId(1L);
        profesor.setRutP("11111111-1");
        profesor.setNombreP("Juan Pérez");
        profesor.setEmailP("juan@duoc.cl");
        profesor.setEspecialidad("Programación");

        FacultadResponse facultad = new FacultadResponse();
        facultad.setId(1L);
        facultad.setNombre("Ingeniería");
        facultad.setUbicacion("Campus Norte");

        curso = new CursoResponse();
        curso.setId(1L);
        curso.setNombreC("Programación");
        curso.setCantidadE(40);
        curso.setDescripcion("Curso Java");
        curso.setProfesor(profesor);
        curso.setFacultad(facultad);

        dto = new CursoDTO();
        dto.setNombreC("Programación");
        dto.setCantidadE(40);
        dto.setDescripcion("Curso Java");
        dto.setIdProfesor(1L);
        dto.setIdFacultad(1L);
    }

    @Test
    void testCrear() throws Exception {

        when(service.crear(any(CursoDTO.class), anyString()))
                .thenReturn(curso);

        mockMvc.perform(post("/api/v1/cursos")
                        .header("Authorization", "Bearer test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(service).crear(any(CursoDTO.class), anyString());
    }

    @Test
    void testListar() throws Exception {

        when(service.listar(anyString()))
                .thenReturn(List.of(curso));

        mockMvc.perform(get("/api/v1/cursos")
                        .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());

        verify(service).listar(anyString());
    }

    @Test
    void testObtener() throws Exception {

        when(service.obtener(eq(1L), anyString()))
                .thenReturn(curso);

        mockMvc.perform(get("/api/v1/cursos/1")
                        .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());

        verify(service, atLeastOnce()).obtener(eq(1L), anyString());
    }

    @Test
    void testActualizar() throws Exception {

        when(service.actualizar(eq(1L), any(CursoDTO.class), anyString()))
                .thenReturn(curso);

        mockMvc.perform(put("/api/v1/cursos/1")
                        .header("Authorization", "Bearer test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(service).actualizar(eq(1L), any(CursoDTO.class), anyString());
    }

    @Test
    void testEliminar() throws Exception {

        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/cursos/1"))
                .andExpect(status().isOk());

        verify(service).eliminar(1L);
    }

    @Test
    void testBuscarPorNombre() throws Exception {

        when(service.buscarPorNombre(eq("Programación"), anyString()))
                .thenReturn(List.of(curso));

        mockMvc.perform(get("/api/v1/cursos/nombre/Programación")
                        .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());

        verify(service).buscarPorNombre(eq("Programación"), anyString());
    }

    @Test
    void testBuscarPorProfesor() throws Exception {

        when(service.buscarPorProfesor(eq(1L), anyString()))
                .thenReturn(List.of(curso));

        mockMvc.perform(get("/api/v1/cursos/profesor/1")
                        .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());

        verify(service).buscarPorProfesor(eq(1L), anyString());
    }

    @Test
    void testBuscarPorFacultad() throws Exception {

        when(service.buscarPorFacultad(eq(1L), anyString()))
                .thenReturn(List.of(curso));

        mockMvc.perform(get("/api/v1/cursos/facultad/1")
                        .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());

        verify(service).buscarPorFacultad(eq(1L), anyString());
    }
}