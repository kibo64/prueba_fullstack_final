package com.example.ms_evaluaciones.controller;

import com.example.ms_evaluaciones.dto.*;
import com.example.ms_evaluaciones.security.JwtFilter;
import com.example.ms_evaluaciones.security.JwtUtil;
import com.example.ms_evaluaciones.service.EvaluacionService;
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

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EvaluacionController.class)
@AutoConfigureMockMvc(addFilters = false)
class EvaluacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper =
            new ObjectMapper().findAndRegisterModules();

    @MockitoBean
    private EvaluacionService service;

    @MockitoBean
    private JwtFilter jwtFilter;

    @MockitoBean
    private JwtUtil jwtUtil;

    private EvaluacionResponse response;
    private EvaluacionDTO dto;

        @BeforeEach
        void setUp() {

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        response = new EvaluacionResponse();
        response.setId(1L);
        response.setNombreE("Prueba 1");
        response.setFecha(LocalDate.now());
        response.setPonderacion(30.0);
        response.setCurso(curso);

        dto = new EvaluacionDTO();
        dto.setNombreE("Prueba 1");
        dto.setFecha(LocalDate.now());
        dto.setPonderacion(30.0);
        dto.setIdCurso(1L);
        }

    @Test
    @WithMockUser(roles = "PROFESOR")
    void testCrear() throws Exception {

        when(service.crear(any(EvaluacionDTO.class), anyString()))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/evaluaciones")
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

        mockMvc.perform(get("/api/v1/evaluaciones")
                        .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ESTUDIANTE")
    void testObtener() throws Exception {

        when(service.obtener(eq(1L), anyString()))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/evaluaciones/1")
                        .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PROFESOR")
    void testActualizar() throws Exception {

        when(service.actualizar(
                eq(1L),
                any(EvaluacionDTO.class),
                anyString()))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/evaluaciones/1")
                        .header("Authorization", "Bearer test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PROFESOR")
    void testEliminar() throws Exception {

        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/evaluaciones/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ESTUDIANTE")
    void testBuscarPorNombre() throws Exception {

        when(service.buscarPorNombre(
                eq("Prueba 1"),
                anyString()))
                .thenReturn(List.of(response));

        mockMvc.perform(
                        get("/api/v1/evaluaciones/nombre/Prueba 1")
                                .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ESTUDIANTE")
    void testBuscarPorCurso() throws Exception {

        when(service.buscarPorCurso(
                eq(1L),
                anyString()))
                .thenReturn(List.of(response));

        mockMvc.perform(
                        get("/api/v1/evaluaciones/curso/1")
                                .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());
    }
}