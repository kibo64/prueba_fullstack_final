package com.example.ms_estudiantes.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.ms_estudiantes.dto.*;
import com.example.ms_estudiantes.service.EstudianteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
        name = "Estudiantes",
        description = "Lista de acciones con estudiantes"
)
@RestController
@RequestMapping("/api/v1/estudiantes")
@RequiredArgsConstructor
public class EstudianteController {

    private final EstudianteService service;

        @Operation(
        summary = "Crear estudiante",
        description = "Permite crear un nuevo estudiante"
        )
    @PostMapping
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<ApiResponse<EstudianteResponse>> crear(
            @Valid @RequestBody EstudianteDTO dto,
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.status(201).body(
                ApiResponse.<EstudianteResponse>builder()
                        .success(true)
                        .message("Estudiante creado")
                        .data(service.crear(dto, token))
                        .build()
                );
        }

        @Operation(
        summary = "Listar estudiantes",
        description = "Obtiene todos los estudiantes registrados"
        )
    @GetMapping
    @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
    public ResponseEntity<ApiResponse<List<EstudianteResponse>>> listar(
            @Parameter(hidden = true)
                @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<EstudianteResponse>>builder()
                        .success(true)
                        .data(service.listar(token))
                        .build()
                );
        }

        @Operation(
                summary = "Buscar estudiante por ID",
                description = "Obtiene un estudiante según su identificador"
        )
        @GetMapping("/{id}")
        @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
        public ResponseEntity<EntityModel<ApiResponse<EstudianteResponse>>> obtener(

                @Parameter(
                        description = "ID del estudiante",
                        example = "1"
                )
                @PathVariable Long id,

                @Parameter(hidden = true)
                @RequestHeader("Authorization") String token) {

        EstudianteResponse estudiante = service.obtener(id, token);

        ApiResponse<EstudianteResponse> response =
                ApiResponse.<EstudianteResponse>builder()
                        .success(true)
                        .message("Estudiante obtenido. Si posee rol PROFESOR también puede utilizar los métodos de actualizar y eliminar este recurso.")
                        .data(estudiante)
                        .build();

        EntityModel<ApiResponse<EstudianteResponse>> recurso =
                EntityModel.of(response);

        recurso.add(
                linkTo(
                        methodOn(EstudianteController.class)
                                .obtener(id, null)
                ).withSelfRel()
        );

        recurso.add(
                linkTo(
                        methodOn(EstudianteController.class)
                                .listar(null)
                ).withRel("all")
        );

        recurso.add(
                linkTo(
                        methodOn(EstudianteController.class)
                                .buscarPorNombre(
                                        estudiante.getNombre(),
                                        null
                                )
                ).withRel("buscar-por-nombre")
        );

        recurso.add(
                linkTo(
                        methodOn(EstudianteController.class)
                                .buscarPorCarrera(
                                        estudiante.getCarrera(),
                                        null
                                )
                ).withRel("buscar-por-carrera")
        );

        recurso.add(
                linkTo(
                        methodOn(EstudianteController.class)
                                .buscarPorFacultad(
                                        estudiante.getFacultad().getId(),
                                        null
                                )
                ).withRel("buscar-por-facultad")
        );

        recurso.add(
                linkTo(
                        methodOn(EstudianteController.class)
                                .actualizar(id, null, null)
                ).withRel("update (requiere rol PROFESOR)")
        );

        recurso.add(
                linkTo(
                        methodOn(EstudianteController.class)
                                .eliminar(id)
                ).withRel("delete (requiere rol PROFESOR)")
        );

        return ResponseEntity.ok(recurso);
        }

        @Operation(
                summary = "Buscar estudiantes por nombre",
                description = "Busca estudiantes utilizando su nombre"
        )
        @GetMapping("/nombre/{nombre}")
        @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
        public ResponseEntity<ApiResponse<List<EstudianteResponse>>> buscarPorNombre(

                @Parameter(
                        description = "Nombre del estudiante",
                        example = "Juan"
                )
                @PathVariable String nombre,
                @Parameter(hidden = true)
                @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<EstudianteResponse>>builder()
                        .success(true)
                        .message("Estudiantes encontrados por nombre")
                        .data(service.buscarPorNombre(nombre, token))
                        .build()
                );
        }

        @Operation(
                summary = "Buscar estudiantes por carrera",
                description = "Busca estudiantes utilizando su carrera"
        )
        @GetMapping("/carrera/{carrera}")
        @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
        public ResponseEntity<ApiResponse<List<EstudianteResponse>>> buscarPorCarrera(

                @Parameter(
                        description = "Carrera del estudiante",
                        example = "Ingeniería Informática"
                )
                @PathVariable String carrera,
                @Parameter(hidden = true)
                @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<EstudianteResponse>>builder()
                        .success(true)
                        .message("Estudiantes encontrados por carrera")
                        .data(service.buscarPorCarrera(carrera, token))
                        .build()
                );
        }

        @Operation(
        summary = "Buscar estudiantes por facultad",
        description = "Busca estudiantes utilizando el ID de la facultad"
        )
        @GetMapping("/facultad/{idFacultad}")
        @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
        public ResponseEntity<ApiResponse<List<EstudianteResponse>>> buscarPorFacultad(

                @Parameter(
                        description = "ID de la facultad",
                        example = "1"
                )
                @PathVariable Long idFacultad,

                @Parameter(hidden = true)
                @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<EstudianteResponse>>builder()
                        .success(true)
                        .data(service.buscarPorFacultad(idFacultad, token))
                        .build()
                );
       }

        @Operation(
                summary = "Actualizar estudiante",
                description = "Actualiza un estudiante existente"
        )
        @PutMapping("/{id}")
        @PreAuthorize("hasRole('PROFESOR')")
        public ResponseEntity<ApiResponse<EstudianteResponse>> actualizar(

                @Parameter(
                        description = "ID del estudiante",
                        example = "1"
                )
                @PathVariable Long id,

                @Valid @RequestBody EstudianteDTO dto,
                @Parameter(hidden = true)
                @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<EstudianteResponse>builder()
                        .success(true)
                        .message("Estudiante actualizado")
                        .data(service.actualizar(id, dto, token))
                        .build()
                );
        }

        @Operation(
                summary = "Eliminar estudiante",
                description = "Elimina un estudiante mediante su identificador"
        )
        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('PROFESOR')")
        public ResponseEntity<ApiResponse<Void>> eliminar(

                @Parameter(
                        description = "ID del estudiante",
                        example = "1"
                )
                @PathVariable Long id) {

        service.eliminar(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Estudiante eliminado")
                        .build()
                );
        }
}