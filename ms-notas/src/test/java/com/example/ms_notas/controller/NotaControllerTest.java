package com.example.ms_notas.controller;

import com.example.ms_notas.dto.*;
import com.example.ms_notas.security.JwtFilter;
import com.example.ms_notas.security.JwtUtil;
import com.example.ms_notas.service.NotaService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotaController.class)
@AutoConfigureMockMvc(addFilters = false)
class NotaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper =
            new ObjectMapper().findAndRegisterModules();

    @MockitoBean
    private NotaService service;

    @MockitoBean
    private JwtFilter jwtFilter;

    @MockitoBean
    private JwtUtil jwtUtil;

    private NotaResponse response;
    private NotaDTO dto;

    @BeforeEach
    void setUp() {

        EstudianteResponse estudiante =
                new EstudianteResponse();
        estudiante.setId(1L);

        EvaluacionResponse evaluacion =
                new EvaluacionResponse();
        evaluacion.setId(1L);

        response = new NotaResponse();
        response.setId(1L);
        response.setNota(6.5);
        response.setEstudiante(estudiante);
        response.setEvaluacion(evaluacion);

        dto = new NotaDTO();
        dto.setNota(6.5);
        dto.setIdEstudiante(1L);
        dto.setIdEvaluacion(1L);
    }

    @Test
    @WithMockUser(roles = "PROFESOR")
    void testCrear() throws Exception {

        when(service.crear(any(NotaDTO.class), anyString()))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/notas")
                        .header("Authorization", "Bearer test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ESTUDIANTE")
    void testListar() throws Exception {

        when(service.listar(anyString()))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/notas")
                        .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ESTUDIANTE")
    void testObtener() throws Exception {

        when(service.obtener(eq(1L), anyString()))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/notas/1")
                        .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PROFESOR")
    void testActualizar() throws Exception {

        when(service.actualizar(
                eq(1L),
                any(NotaDTO.class),
                anyString()))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/notas/1")
                        .header("Authorization", "Bearer test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PROFESOR")
    void testEliminar() throws Exception {

        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/notas/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ESTUDIANTE")
    void testBuscarPorEstudiante() throws Exception {

        when(service.buscarPorEstudiante(
                eq(1L),
                anyString()))
                .thenReturn(List.of(response));

        mockMvc.perform(
                        get("/api/v1/notas/estudiante/1")
                                .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ESTUDIANTE")
    void testBuscarPorEvaluacion() throws Exception {

        when(service.buscarPorEvaluacion(
                eq(1L),
                anyString()))
                .thenReturn(List.of(response));

        mockMvc.perform(
                        get("/api/v1/notas/evaluacion/1")
                                .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ESTUDIANTE")
    void testPromedioPorEstudiante() throws Exception {

        when(service.promedioPorEstudiante(1L))
                .thenReturn(6.2);

        mockMvc.perform(
                        get("/api/v1/notas/Promedio/1"))
                .andExpect(status().isOk());
    }
}