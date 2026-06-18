package com.example.ms_historial_academico.controller;

import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.ms_historial_academico.dto.ApiResponse;
import com.example.ms_historial_academico.dto.HistorialAcademicoDTO;
import com.example.ms_historial_academico.dto.HistorialAcademicoResponse;
import com.example.ms_historial_academico.service.HistorialAcademicoService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
        name = "Historial Académico",
        description = "Lista de acciones con historial académico"
)
@RestController
@RequestMapping("/api/v1/historial-academico")
@RequiredArgsConstructor
public class HistorialAcademicoController {

    private final HistorialAcademicoService service;

        @Operation(
                summary = "Crear historial académico",
                description = "Permite crear un nuevo historial académico"
        )
        @PostMapping
        @PreAuthorize("hasRole('PROFESOR')")
        public ResponseEntity<ApiResponse<HistorialAcademicoResponse>> crear(
                @Valid @RequestBody HistorialAcademicoDTO dto,

                @Parameter(hidden = true)
                @RequestHeader("Authorization") String token) {

        return ResponseEntity.status(201).body(
                ApiResponse.<HistorialAcademicoResponse>builder()
                        .success(true)
                        .message("Historial académico creado")
                        .data(service.crear(dto, token))
                        .build()
                );
        }

        @Operation(
                summary = "Listar historial académico",
                description = "Obtiene todos los registros del historial académico"
        )
        @GetMapping
        @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
        public ResponseEntity<ApiResponse<List<HistorialAcademicoResponse>>> listar(

                @Parameter(hidden = true)
                @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<HistorialAcademicoResponse>>builder()
                        .success(true)
                        .message("Listado obtenido")
                        .data(service.listar(token))
                        .build()
                );
        }

        @Operation(
                summary = "Buscar historial académico por ID",
                description = "Obtiene un historial académico según su identificador"
        )
        @GetMapping("/{id}")
        @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
        public ResponseEntity<EntityModel<ApiResponse<HistorialAcademicoResponse>>> obtener(

                @Parameter(
                        description = "ID del historial académico",
                        example = "1"
                )
                @PathVariable Long id,

                @Parameter(hidden = true)
                @RequestHeader("Authorization") String token) {

        HistorialAcademicoResponse historial =
                service.obtener(id, token);

        ApiResponse<HistorialAcademicoResponse> response =
                ApiResponse.<HistorialAcademicoResponse>builder()
                        .success(true)
                        .message("Historial académico obtenido. Si posee rol PROFESOR también puede utilizar los métodos de actualizar y eliminar este recurso.")
                        .data(historial)
                        .build();

        EntityModel<ApiResponse<HistorialAcademicoResponse>> recurso =
                EntityModel.of(response);

        recurso.add(
                linkTo(
                        methodOn(HistorialAcademicoController.class)
                                .obtener(id, null)
                ).withSelfRel()
        );

        recurso.add(
                linkTo(
                        methodOn(HistorialAcademicoController.class)
                                .listar(null)
                ).withRel("all")
        );

        recurso.add(
                linkTo(
                        methodOn(HistorialAcademicoController.class)
                                .buscarPorEstudiante(
                                        historial.getEstudiante().getId(),
                                        null
                                )
                ).withRel("buscar-por-estudiante")
        );

        recurso.add(
                linkTo(
                        methodOn(HistorialAcademicoController.class)
                                .buscarPorCurso(
                                        historial.getCurso().getId(),
                                        null
                                )
                ).withRel("buscar-por-curso")
        );

        recurso.add(
                linkTo(
                        methodOn(HistorialAcademicoController.class)
                                .buscarPorEstado(
                                        historial.getEstado(),
                                        null
                                )
                ).withRel("buscar-por-estado")
        );

        recurso.add(
                linkTo(
                        methodOn(HistorialAcademicoController.class)
                                .actualizar(id, null, null)
                ).withRel("update (requiere rol PROFESOR)")
        );

        recurso.add(
                linkTo(
                        methodOn(HistorialAcademicoController.class)
                                .eliminar(id)
                ).withRel("delete (requiere rol PROFESOR)")
        );

        return ResponseEntity.ok(recurso);
        }

        @Operation(
                summary = "Actualizar historial académico",
                description = "Actualiza un historial académico existente"
        )

        @PutMapping("/{id}")
        @PreAuthorize("hasRole('PROFESOR')")
        public ResponseEntity<ApiResponse<HistorialAcademicoResponse>> actualizar(

                @Parameter(
                        description = "ID del historial académico",
                        example = "1"
                )
                @PathVariable Long id,

                @Valid @RequestBody HistorialAcademicoDTO dto,

                @Parameter(hidden = true)
                @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<HistorialAcademicoResponse>builder()
                        .success(true)
                        .message("Historial académico actualizado")
                        .data(service.actualizar(id, dto, token))
                        .build()
                );
        }

        @Operation(
                summary = "Eliminar historial académico",
                description = "Elimina un historial académico mediante su identificador"
        )
        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('PROFESOR')")
        public ResponseEntity<ApiResponse<Void>> eliminar(

                @Parameter(
                        description = "ID del historial académico",
                        example = "1"
                )
                @PathVariable Long id) {

        service.eliminar(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Historial académico eliminado")
                        .build()
                );
        }

        @Operation(
                summary = "Buscar historial por estudiante",
                description = "Busca registros utilizando el ID del estudiante"
        )
        @GetMapping("/estudiante/{idEstudiante}")
        @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
        public ResponseEntity<ApiResponse<List<HistorialAcademicoResponse>>> buscarPorEstudiante(

                @Parameter(
                        description = "ID del estudiante",
                        example = "1"
                )
                @PathVariable Long idEstudiante,

                @Parameter(hidden = true)
                @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<HistorialAcademicoResponse>>builder()
                        .success(true)
                        .message("Historial académico encontrado por estudiante")
                        .data(service.buscarPorEstudiante(idEstudiante, token))
                        .build()
                );
        }

        @Operation(
                summary = "Buscar historial por curso",
                description = "Busca registros utilizando el ID del curso"
        )
        @GetMapping("/curso/{idCurso}")
        @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
        public ResponseEntity<ApiResponse<List<HistorialAcademicoResponse>>> buscarPorCurso(

                @Parameter(
                        description = "ID del curso",
                        example = "1"
                )
                @PathVariable Long idCurso,

                @Parameter(hidden = true)
                @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<HistorialAcademicoResponse>>builder()
                        .success(true)
                        .message("Historial académico encontrado por curso")
                        .data(service.buscarPorCurso(idCurso, token))
                        .build()
                );
        }

        @Operation(
                summary = "Buscar historial por estado",
                description = "Busca registros utilizando el estado académico"
        )
        @GetMapping("/estado/{estado}")
        @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
        public ResponseEntity<ApiResponse<List<HistorialAcademicoResponse>>> buscarPorEstado(

                @Parameter(
                        description = "Estado académico",
                        example = "APROBADO"
                )
                @PathVariable String estado,

                @Parameter(hidden = true)
                @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<HistorialAcademicoResponse>>builder()
                        .success(true)
                        .message("Historial académico encontrado por estado")
                        .data(service.buscarPorEstado(estado, token))
                        .build()
                );
        }
}