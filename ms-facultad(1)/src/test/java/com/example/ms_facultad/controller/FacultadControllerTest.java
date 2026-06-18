package com.example.ms_facultad.controller;

import com.example.ms_facultad.dto.FacultadDTO;
import com.example.ms_facultad.model.Facultad;
import com.example.ms_facultad.security.JwtFilter;
import com.example.ms_facultad.security.JwtUtil;
import com.example.ms_facultad.service.FacultadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultadController.class)
@AutoConfigureMockMvc(addFilters = false)
class FacultadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @MockitoBean
    private FacultadService service;

    @MockitoBean
    private JwtFilter jwtFilter;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser(roles = "PROFESOR")
    void deberiaCrearFacultad() throws Exception {

        FacultadDTO dto = new FacultadDTO();
        dto.setNombre("Ingeniería");
        dto.setUbicacion("Campus Norte");

        Facultad facultad =
                new Facultad(1L, "Ingeniería", "Campus Norte");

        when(service.crear(any(FacultadDTO.class)))
                .thenReturn(facultad);

        mockMvc.perform(post("/api/v1/facultades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ESTUDIANTE")
    void deberiaListarFacultades() throws Exception {

        when(service.listar()).thenReturn(
                List.of(
                        new Facultad(
                                1L,
                                "Ingeniería",
                                "Campus Norte"
                        )
                )
        );

        mockMvc.perform(get("/api/v1/facultades"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ESTUDIANTE")
    void deberiaObtenerFacultad() throws Exception {

        when(service.obtener(1L))
                .thenReturn(
                        new Facultad(
                                1L,
                                "Ingeniería",
                                "Campus Norte"
                        )
                );

        mockMvc.perform(get("/api/v1/facultades/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PROFESOR")
    void deberiaActualizarFacultad() throws Exception {

        FacultadDTO dto = new FacultadDTO();
        dto.setNombre("Ingeniería");
        dto.setUbicacion("Campus Norte");

        when(service.actualizar(
                eq(1L),
                any(FacultadDTO.class)))
                .thenReturn(
                        new Facultad(
                                1L,
                                "Ingeniería",
                                "Campus Norte"
                        )
                );

        mockMvc.perform(
                        put("/api/v1/facultades/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ESTUDIANTE")
    void deberiaBuscarPorNombre() throws Exception {

        when(service.buscarPorNombre("Ingeniería"))
                .thenReturn(
                        List.of(
                                new Facultad(
                                        1L,
                                        "Ingeniería",
                                        "Campus Norte"
                                )
                        )
                );

        mockMvc.perform(
                        get("/api/v1/facultades/nombre/Ingeniería"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ESTUDIANTE")
    void deberiaBuscarPorUbicacion() throws Exception {

        when(service.buscarPorUbicacion("Campus Norte"))
                .thenReturn(
                        List.of(
                                new Facultad(
                                        1L,
                                        "Ingeniería",
                                        "Campus Norte"
                                )
                        )
                );

        mockMvc.perform(
                        get("/api/v1/facultades/ubicacion/Campus%20Norte"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PROFESOR")
    void deberiaEliminarFacultad() throws Exception {

        doNothing().when(service).eliminar(1L);

        mockMvc.perform(
                        delete("/api/v1/facultades/1"))
                .andExpect(status().isOk());
    }
}
