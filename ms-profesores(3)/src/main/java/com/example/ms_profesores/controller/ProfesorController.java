package com.example.ms_profesores.controller;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.ms_profesores.dto.*;
import com.example.ms_profesores.service.ProfesorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/v1/profesores")
@RequiredArgsConstructor
public class ProfesorController {

    private final ProfesorService service;

        @Operation(
                summary = "Crear profesor",
                description = "Permite crear un nuevo profesor"
        )
        @PostMapping
        @PreAuthorize("hasRole('PROFESOR')")
        public ResponseEntity<ApiResponse<ProfesorResponse>> crear(

                @Valid @RequestBody ProfesorDTO dto,

                @Parameter(hidden = true)
                @RequestHeader("Authorization") String token) {

        return ResponseEntity.status(201).body(
                ApiResponse.<ProfesorResponse>builder()
                        .success(true)
                        .message("Profesor creado")
                        .data(service.crear(dto, token))
                        .build()
                );
        }

        @Operation(
                summary = "Listar profesores",
                description = "Obtiene todos los profesores registrados"
        )
        @GetMapping
        @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
        public ResponseEntity<ApiResponse<List<ProfesorResponse>>> listar(

                @Parameter(hidden = true)
                @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<ProfesorResponse>>builder()
                        .success(true)
                        .message("Listado obtenido")
                        .data(service.listar(token))
                        .build()
                );
        }
        
        @Operation(
                summary = "Buscar profesor por ID",
                description = "Obtiene un profesor según su identificador"
        )
        @GetMapping("/{id}")
        @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
        public ResponseEntity<EntityModel<ApiResponse<ProfesorResponse>>> obtener(

                @Parameter(
                        description = "ID del profesor",
                        example = "1"
                )
                @PathVariable Long id,

                @Parameter(hidden = true)
                @RequestHeader("Authorization") String token) {

        ProfesorResponse profesor = service.obtener(id, token);

        ApiResponse<ProfesorResponse> response =
                ApiResponse.<ProfesorResponse>builder()
                        .success(true)
                        .message("Profesor obtenido. Si posee rol PROFESOR también puede utilizar los métodos de actualizar y eliminar este recurso.")
                        .data(profesor)
                        .build();

        EntityModel<ApiResponse<ProfesorResponse>> recurso =
                EntityModel.of(response);

        recurso.add(
                linkTo(
                        methodOn(ProfesorController.class)
                                .obtener(id, null)
                ).withSelfRel()
        );

        recurso.add(
                linkTo(
                        methodOn(ProfesorController.class)
                                .listar(null)
                ).withRel("all")
        );

        recurso.add(
                linkTo(
                        methodOn(ProfesorController.class)
                                .buscarPorNombre(
                                        profesor.getNombreP(),
                                        null
                                )
                ).withRel("buscar-por-nombre")
        );

        recurso.add(
                linkTo(
                        methodOn(ProfesorController.class)
                                .buscarPorEspecialidad(
                                        profesor.getEspecialidad(),
                                        null
                                )
                ).withRel("buscar-por-especialidad")
        );

        recurso.add(
                linkTo(
                        methodOn(ProfesorController.class)
                                .buscarPorFacultad(
                                        profesor.getFacultad().getId(),
                                        null
                                )
                ).withRel("buscar-por-facultad")
        );

        recurso.add(
                linkTo(
                        methodOn(ProfesorController.class)
                                .actualizar(id, null, null)
                ).withRel("update (requiere rol PROFESOR)")
        );

        recurso.add(
                linkTo(
                        methodOn(ProfesorController.class)
                                .eliminar(id)
                ).withRel("delete (requiere rol PROFESOR)")
        );

        return ResponseEntity.ok(recurso);
        }



        @Operation(
                summary = "Actualizar profesor",
                description = "Actualiza un profesor existente"
        )
        @PutMapping("/{id}")
        @PreAuthorize("hasRole('PROFESOR')")
        public ResponseEntity<ApiResponse<ProfesorResponse>> actualizar(

                @Parameter(
                        description = "ID del profesor",
                        example = "1"
                )
                @PathVariable Long id,

                @Valid @RequestBody ProfesorDTO dto,

                @Parameter(hidden = true)
                @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<ProfesorResponse>builder()
                        .success(true)
                        .message("Profesor actualizado")
                        .data(service.actualizar(id, dto, token))
                        .build()
                );
        }

        @Operation(
                summary = "Eliminar profesor",
                description = "Elimina un profesor mediante su identificador"
        )
        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('PROFESOR')")
        public ResponseEntity<ApiResponse<Void>> eliminar(

                @Parameter(
                        description = "ID del profesor",
                        example = "1"
                )
                @PathVariable Long id) {

        service.eliminar(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Profesor eliminado")
                        .build()
                );
        }

        @Operation(
                summary = "Buscar profesores por nombre",
                description = "Busca profesores utilizando su nombre"
        )
        @GetMapping("/nombre/{nombreP}")
        @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
        public ResponseEntity<ApiResponse<List<ProfesorResponse>>> buscarPorNombre(

                @Parameter(
                        description = "Nombre del profesor",
                        example = "Juan"
                )
                @PathVariable String nombreP,

                @Parameter(hidden = true)
                @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<ProfesorResponse>>builder()
                        .success(true)
                        .message("Profesores encontrados por nombre")
                        .data(service.buscarPorNombre(nombreP, token))
                        .build()
                );
        }

        @Operation(
                summary = "Buscar profesores por especialidad",
                description = "Busca profesores utilizando su especialidad"
        )
        @GetMapping("/especialidad/{especialidad}")
        @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
        public ResponseEntity<ApiResponse<List<ProfesorResponse>>> buscarPorEspecialidad(

                @Parameter(
                        description = "Especialidad del profesor",
                        example = "Matemáticas"
                )
                @PathVariable String especialidad,

                @Parameter(hidden = true)
                @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<ProfesorResponse>>builder()
                        .success(true)
                        .message("Profesores encontrados por especialidad")
                        .data(service.buscarPorEspecialidad(especialidad, token))
                        .build()
                );
        }

        @Operation(
                summary = "Buscar profesores por facultad",
                description = "Busca profesores utilizando el ID de la facultad"
        )
        @GetMapping("/facultad/{idFacultad}")
        @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
        public ResponseEntity<ApiResponse<List<ProfesorResponse>>> buscarPorFacultad(

                @Parameter(
                        description = "ID de la facultad",
                        example = "1"
                )
                @PathVariable Long idFacultad,

                @Parameter(hidden = true)
                @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<ProfesorResponse>>builder()
                        .success(true)
                        .message("Profesores encontrados por facultad")
                        .data(service.buscarPorFacultad(idFacultad, token))
                        .build()
                );
        }
}