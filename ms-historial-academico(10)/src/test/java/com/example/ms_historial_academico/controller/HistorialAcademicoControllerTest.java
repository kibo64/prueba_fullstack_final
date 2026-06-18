package com.example.ms_historial_academico.controller;

import com.example.ms_historial_academico.dto.*;
import com.example.ms_historial_academico.security.JwtFilter;
import com.example.ms_historial_academico.security.JwtUtil;
import com.example.ms_historial_academico.service.HistorialAcademicoService;
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

@WebMvcTest(HistorialAcademicoController.class)
@AutoConfigureMockMvc(addFilters = false)
class HistorialAcademicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper =
            new ObjectMapper().findAndRegisterModules();

    @MockitoBean
    private HistorialAcademicoService service;

    @MockitoBean
    private JwtFilter jwtFilter;

    @MockitoBean
    private JwtUtil jwtUtil;

    private HistorialAcademicoResponse response;
    private HistorialAcademicoDTO dto;

    @BeforeEach
    void setUp() {

        EstudianteResponse estudiante =
                new EstudianteResponse();
        estudiante.setId(1L);

        CursoResponse curso =
                new CursoResponse();
        curso.setId(1L);

        response = new HistorialAcademicoResponse();
        response.setId(1L);
        response.setPromedioFinal(5.8);
        response.setAsistenciaFinal(90.0);
        response.setEstado("APROBADO");
        response.setEstudiante(estudiante);
        response.setCurso(curso);

        dto = new HistorialAcademicoDTO();
        dto.setPromedioFinal(5.8);
        dto.setAsistenciaFinal(90.0);
        dto.setIdEstudiante(1L);
        dto.setIdCurso(1L);
    }

    @Test
    @WithMockUser(roles = "PROFESOR")
    void testCrear() throws Exception {

        when(service.crear(
                any(HistorialAcademicoDTO.class),
                anyString()))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/v1/historial-academico")
                                .header("Authorization", "Bearer test")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ESTUDIANTE")
    void testListar() throws Exception {

        when(service.listar(anyString()))
                .thenReturn(List.of(response));

        mockMvc.perform(
                        get("/api/v1/historial-academico")
                                .header("Authorization", "Bearer test")
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ESTUDIANTE")
    void testObtener() throws Exception {

        when(service.obtener(
                eq(1L),
                anyString()))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/v1/historial-academico/1")
                                .header("Authorization", "Bearer test")
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PROFESOR")
    void testActualizar() throws Exception {

        when(service.actualizar(
                eq(1L),
                any(HistorialAcademicoDTO.class),
                anyString()))
                .thenReturn(response);

        mockMvc.perform(
                        put("/api/v1/historial-academico/1")
                                .header("Authorization", "Bearer test")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PROFESOR")
    void testEliminar() throws Exception {

        doNothing().when(service)
                .eliminar(1L);

        mockMvc.perform(
                        delete("/api/v1/historial-academico/1"))
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
                        get("/api/v1/historial-academico/estudiante/1")
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
                        get("/api/v1/historial-academico/curso/1")
                                .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ESTUDIANTE")
    void testBuscarPorEstado() throws Exception {

        when(service.buscarPorEstado(
                eq("APROBADO"),
                anyString()))
                .thenReturn(List.of(response));

        mockMvc.perform(
                        get("/api/v1/historial-academico/estado/APROBADO")
                                .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());
    }
}