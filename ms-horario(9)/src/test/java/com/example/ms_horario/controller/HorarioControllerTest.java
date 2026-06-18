package com.example.ms_horario.controller;

import com.example.ms_horario.dto.*;
import com.example.ms_horario.security.JwtFilter;
import com.example.ms_horario.security.JwtUtil;
import com.example.ms_horario.service.HorarioService;
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

@WebMvcTest(HorarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class HorarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper =
            new ObjectMapper().findAndRegisterModules();

    @MockitoBean
    private HorarioService service;

    @MockitoBean
    private JwtFilter jwtFilter;

    @MockitoBean
    private JwtUtil jwtUtil;

    private HorarioResponse response;
    private HorarioDTO dto;

    @BeforeEach
    void setUp() {

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        response = new HorarioResponse();
        response.setId(1L);
        response.setDia("Lunes");
        response.setHoraInicio("08:00");
        response.setHoraFin("10:00");
        response.setSala("A101");
        response.setCurso(curso);

        dto = new HorarioDTO();
        dto.setDia("Lunes");
        dto.setHoraInicio("08:00");
        dto.setHoraFin("10:00");
        dto.setSala("A101");
        dto.setIdCurso(1L);
    }

    @Test
    @WithMockUser(roles = "PROFESOR")
    void testCrear() throws Exception {

        when(service.crear(any(HorarioDTO.class), anyString()))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/horarios")
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

        mockMvc.perform(get("/api/v1/horarios")
                        .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ESTUDIANTE")
    void testObtener() throws Exception {

        when(service.obtener(eq(1L), anyString()))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/horarios/1")
                        .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PROFESOR")
    void testActualizar() throws Exception {

        when(service.actualizar(
                eq(1L),
                any(HorarioDTO.class),
                anyString()))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/horarios/1")
                        .header("Authorization", "Bearer test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PROFESOR")
    void testEliminar() throws Exception {

        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/horarios/1"))
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
                        get("/api/v1/horarios/curso/1")
                                .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ESTUDIANTE")
    void testBuscarPorDia() throws Exception {

        when(service.buscarPorDia(
                eq("Lunes"),
                anyString()))
                .thenReturn(List.of(response));

        mockMvc.perform(
                        get("/api/v1/horarios/dia/Lunes")
                                .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());
    }
}