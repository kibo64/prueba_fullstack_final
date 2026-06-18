package com.example.ms_estudiantes.controller;

import com.example.ms_estudiantes.dto.EstudianteDTO;
import com.example.ms_estudiantes.dto.EstudianteResponse;
import com.example.ms_estudiantes.dto.FacultadResponse;
import com.example.ms_estudiantes.security.JwtFilter;
import com.example.ms_estudiantes.security.JwtUtil;
import com.example.ms_estudiantes.service.EstudianteService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EstudianteController.class)
@AutoConfigureMockMvc(addFilters = false)
class EstudianteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private EstudianteService service;

    @MockitoBean
    private JwtFilter jwtFilter;

    @MockitoBean
    private JwtUtil jwtUtil;

    private EstudianteResponse estudiante;
    private EstudianteDTO dto;

    @BeforeEach
    void setUp() {

        FacultadResponse facultad = new FacultadResponse();
        facultad.setId(1L);
        facultad.setNombre("Ingeniería");
        facultad.setUbicacion("Campus Norte");

        estudiante = EstudianteResponse.builder()
                .id(1L)
                .rut("11111111-1")
                .nombre("Juan")
                .email("juan@gmail.com")
                .carrera("Ingeniería")
                .facultad(facultad)
                .build();

        dto = new EstudianteDTO();
        dto.setRut("11111111-1");
        dto.setNombre("Juan");
        dto.setEmail("juan@gmail.com");
        dto.setCarrera("Ingeniería");
        dto.setIdFacultad(1L);
    }

    @Test
    void testCrear() throws Exception {

        when(service.crear(any(EstudianteDTO.class), any()))
                .thenReturn(estudiante);

        mockMvc.perform(post("/api/v1/estudiantes")
                        .header("Authorization", "Bearer test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(service).crear(any(EstudianteDTO.class), any());
    }

    @Test
    void testListar() throws Exception {

        when(service.listar(any()))
                .thenReturn(List.of(estudiante));

        mockMvc.perform(get("/api/v1/estudiantes")
                        .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());

        verify(service).listar(any());
    }

    @Test
    void testObtener() throws Exception {

        when(service.obtener(1L, "Bearer test"))
                .thenReturn(estudiante);

        mockMvc.perform(get("/api/v1/estudiantes/1")
                        .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());

        verify(service, atLeastOnce()).obtener(1L, "Bearer test");
    }

    @Test
    void testActualizar() throws Exception {

        when(service.actualizar(eq(1L), any(EstudianteDTO.class), any()))
                .thenReturn(estudiante);

        mockMvc.perform(put("/api/v1/estudiantes/1")
                        .header("Authorization", "Bearer test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(service).actualizar(eq(1L), any(EstudianteDTO.class), any());
    }

    @Test
    void testEliminar() throws Exception {

        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/estudiantes/1"))
                .andExpect(status().isOk());

        verify(service).eliminar(1L);
    }

    @Test
    void testBuscarPorNombre() throws Exception {

        when(service.buscarPorNombre("Juan", "Bearer test"))
                .thenReturn(List.of(estudiante));

        mockMvc.perform(get("/api/v1/estudiantes/nombre/Juan")
                        .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());

        verify(service).buscarPorNombre("Juan", "Bearer test");
    }

    @Test
    void testBuscarPorCarrera() throws Exception {

        when(service.buscarPorCarrera("Ingeniería", "Bearer test"))
                .thenReturn(List.of(estudiante));

        mockMvc.perform(get("/api/v1/estudiantes/carrera/Ingeniería")
                        .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());

        verify(service).buscarPorCarrera("Ingeniería", "Bearer test");
    }

    @Test
    void testBuscarPorFacultad() throws Exception {

        when(service.buscarPorFacultad(1L, "Bearer test"))
                .thenReturn(List.of(estudiante));

        mockMvc.perform(get("/api/v1/estudiantes/facultad/1")
                        .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());

        verify(service).buscarPorFacultad(1L, "Bearer test");
    }
}