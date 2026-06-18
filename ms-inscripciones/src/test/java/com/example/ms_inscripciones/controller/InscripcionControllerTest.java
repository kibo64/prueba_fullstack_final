package com.example.ms_inscripciones.controller;

import com.example.ms_inscripciones.dto.*;
import com.example.ms_inscripciones.security.JwtFilter;
import com.example.ms_inscripciones.security.JwtUtil;
import com.example.ms_inscripciones.service.InscripcionService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(InscripcionController.class)
@AutoConfigureMockMvc(addFilters = false)
class InscripcionControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockitoBean
    private InscripcionService service;

    @MockitoBean
    private JwtFilter jwtFilter;

    @MockitoBean
    private JwtUtil jwtUtil;

    private  final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules() ;
    
    private InscripcionResponse response;
    private InscripcionDTO dto;


    @BeforeEach
    void setUp() {

        EstudianteResponse estudiante = new EstudianteResponse();
        estudiante.setId(1L);

        CursoResponse curso = new CursoResponse();
        curso.setId(1L);

        response = new InscripcionResponse();
        response.setId(1L);
        response.setFecha(LocalDate.now());
        response.setEstudiante(estudiante);
        response.setCurso(curso);

        dto = new InscripcionDTO();
        dto.setFecha(LocalDate.now());
        dto.setIdEstudiante(1L);
        dto.setIdCurso(1L);
    }



        @Test
        @WithMockUser(roles = "PROFESOR")
        void testCrear() throws Exception {

        when(service.crear(any(InscripcionDTO.class), anyString()))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/inscripciones")
                        .header("Authorization", "Bearer test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated());
        }

    @Test
    @WithMockUser(roles = "ESTUDIANTE")
    void testListar() throws Exception {

        when(service.listar(anyString()))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/inscripciones")
                        .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ESTUDIANTE")
    void testObtener() throws Exception {

        when(service.obtener(eq(1L), anyString()))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/inscripciones/1")
                        .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PROFESOR")
    void testActualizar() throws Exception {

        when(service.actualizar(eq(1L), any(InscripcionDTO.class), anyString()))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/inscripciones/1")
                        .header("Authorization", "Bearer test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PROFESOR")
    void testEliminar() throws Exception {

        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/inscripciones/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ESTUDIANTE")
    void testBuscarPorEstudiante() throws Exception {

        when(service.buscarPorEstudiante(eq(1L), anyString()))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/inscripciones/estudiante/1")
                        .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ESTUDIANTE")
    void testBuscarPorCurso() throws Exception {

        when(service.buscarPorCurso(eq(1L), anyString()))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/inscripciones/curso/1")
                        .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());
    }
}

