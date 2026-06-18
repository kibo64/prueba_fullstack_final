package com.example.ms_profesores.controller;

import com.example.ms_profesores.dto.*;
import com.example.ms_profesores.security.JwtFilter;
import com.example.ms_profesores.security.JwtUtil;
import com.example.ms_profesores.service.ProfesorService;
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

@WebMvcTest(controllers = ProfesorController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProfesorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private ProfesorService service;

    @MockitoBean
    private JwtFilter jwtFilter;

    @MockitoBean
    private JwtUtil jwtUtil;

    private ProfesorResponse profesor;
    private ProfesorDTO dto;

    @BeforeEach
    void setUp() {

        FacultadResponse facultad = new FacultadResponse();
        facultad.setId(1L);

        profesor = new ProfesorResponse();
        profesor.setId(1L);
        profesor.setRutP("11111111-1");
        profesor.setNombreP("Juan Pérez");
        profesor.setEmailP("juan@duoc.cl");
        profesor.setEspecialidad("Programación");
        profesor.setFacultad(facultad);

        dto = new ProfesorDTO();
        dto.setRutP("11111111-1");
        dto.setNombreP("Juan Pérez");
        dto.setEmailP("juan@duoc.cl");
        dto.setEspecialidad("Programación");
        dto.setIdFacultad(1L);
    }

    @Test
    void testCrear() throws Exception {

        when(service.crear(any(ProfesorDTO.class), anyString()))
                .thenReturn(profesor);

        mockMvc.perform(post("/api/v1/profesores")
                        .header("Authorization", "Bearer test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(service).crear(any(ProfesorDTO.class), anyString());
    }

    @Test
    void testListar() throws Exception {

        when(service.listar(anyString()))
                .thenReturn(List.of(profesor));

        mockMvc.perform(get("/api/v1/profesores")
                        .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());

        verify(service).listar(anyString());
    }

    @Test
    void testObtener() throws Exception {

        when(service.obtener(eq(1L), anyString()))
                .thenReturn(profesor);

        mockMvc.perform(get("/api/v1/profesores/1")
                        .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());

        verify(service, atLeastOnce()).obtener(eq(1L), anyString());
    }

    @Test
    void testActualizar() throws Exception {

        when(service.actualizar(eq(1L), any(ProfesorDTO.class), anyString()))
                .thenReturn(profesor);

        mockMvc.perform(put("/api/v1/profesores/1")
                        .header("Authorization", "Bearer test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(service).actualizar(eq(1L), any(ProfesorDTO.class), anyString());
    }

    @Test
    void testEliminar() throws Exception {

        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/profesores/1"))
                .andExpect(status().isOk());

        verify(service).eliminar(1L);
    }

    @Test
    void testBuscarPorNombre() throws Exception {

        when(service.buscarPorNombre(eq("Juan"), anyString()))
                .thenReturn(List.of(profesor));

        mockMvc.perform(get("/api/v1/profesores/nombre/Juan")
                        .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());

        verify(service).buscarPorNombre(eq("Juan"), anyString());
    }

    @Test
    void testBuscarPorEspecialidad() throws Exception {

        when(service.buscarPorEspecialidad(eq("Programación"), anyString()))
                .thenReturn(List.of(profesor));

        mockMvc.perform(get("/api/v1/profesores/especialidad/Programación")
                        .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());

        verify(service).buscarPorEspecialidad(eq("Programación"), anyString());
    }

    @Test
    void testBuscarPorFacultad() throws Exception {

        when(service.buscarPorFacultad(eq(1L), anyString()))
                .thenReturn(List.of(profesor));

        mockMvc.perform(get("/api/v1/profesores/facultad/1")
                        .header("Authorization", "Bearer test"))
                .andExpect(status().isOk());

        verify(service).buscarPorFacultad(eq(1L), anyString());
    }
}