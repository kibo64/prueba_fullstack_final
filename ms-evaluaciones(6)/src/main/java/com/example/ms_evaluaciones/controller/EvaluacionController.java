package com.example.ms_evaluaciones.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.ms_evaluaciones.dto.ApiResponse;
import com.example.ms_evaluaciones.dto.EvaluacionDTO;
import com.example.ms_evaluaciones.dto.EvaluacionResponse;
import com.example.ms_evaluaciones.service.EvaluacionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
        name = "Evaluaciones",
        description = "Lista de acciones con evaluaciones"
)
@RestController
@RequestMapping("/api/v1/evaluaciones")
@RequiredArgsConstructor
public class EvaluacionController {

    private final EvaluacionService service;

    @Operation(
            summary = "Crear evaluación",
            description = "Permite crear una nueva evaluación"
        )
    @PostMapping
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<ApiResponse<EvaluacionResponse>> crear(

            @Valid @RequestBody EvaluacionDTO dto,

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.status(201).body(
                ApiResponse.<EvaluacionResponse>builder()
                        .success(true)
                        .message("Evaluación creada")
                        .data(service.crear(dto, token))
                        .build()
                );
        }

    @Operation(
            summary = "Listar evaluaciones",
            description = "Obtiene todas las evaluaciones registradas"
        )
    @GetMapping
    @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
    public ResponseEntity<ApiResponse<List<EvaluacionResponse>>> listar(
            
                @Parameter(hidden = true)
                @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<EvaluacionResponse>>builder()
                        .success(true)
                        .message("Listado obtenido")
                        .data(service.listar(token))
                        .build()
                );
        }

        @Operation(
                summary = "Buscar evaluación por ID",
                description = "Obtiene una evaluación según su identificador"
        )
        @GetMapping("/{id}")
        @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
        public ResponseEntity<EntityModel<ApiResponse<EvaluacionResponse>>> obtener(

                @Parameter(
                        description = "ID de la evaluación",
                        example = "1"
                )
                @PathVariable Long id,

                @Parameter(hidden = true)
                @RequestHeader("Authorization") String token) {

        EvaluacionResponse evaluacion = service.obtener(id, token);

        ApiResponse<EvaluacionResponse> response =
                ApiResponse.<EvaluacionResponse>builder()
                        .success(true)
                        .message("Evaluación obtenida. Si posee rol PROFESOR también puede utilizar los métodos de actualizar y eliminar este recurso.")
                        .data(evaluacion)
                        .build();

        EntityModel<ApiResponse<EvaluacionResponse>> recurso =
                EntityModel.of(response);

        recurso.add(
                linkTo(
                        methodOn(EvaluacionController.class)
                                .obtener(id, null)
                ).withSelfRel()
        );

        recurso.add(
                linkTo(
                        methodOn(EvaluacionController.class)
                                .listar(null)
                ).withRel("all")
        );

        recurso.add(
                linkTo(
                        methodOn(EvaluacionController.class)
                                .buscarPorNombre(
                                        evaluacion.getNombreE(),
                                        null
                                )
                ).withRel("buscar-por-nombre")
        );

        recurso.add(
                linkTo(
                        methodOn(EvaluacionController.class)
                                .buscarPorCurso(
                                        evaluacion.getCurso().getId(),
                                        null
                                )
                ).withRel("buscar-por-curso")
        );

        recurso.add(
                linkTo(
                        methodOn(EvaluacionController.class)
                                .actualizar(id, null, null)
                ).withRel("update (requiere rol PROFESOR)")
        );

        recurso.add(
                linkTo(
                        methodOn(EvaluacionController.class)
                                .eliminar(id)
                ).withRel("delete (requiere rol PROFESOR)")
        );

        return ResponseEntity.ok(recurso);
        }

    @Operation(
            summary = "Actualizar evaluación",
            description = "Actualiza una evaluación existente"
        )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<ApiResponse<EvaluacionResponse>> actualizar(

            @Parameter(
                    description = "ID de la evaluación",
                    example = "1"
                )
            @PathVariable Long id,

            @Valid @RequestBody EvaluacionDTO dto,
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<EvaluacionResponse>builder()
                        .success(true)
                        .message("Evaluación actualizada")
                        .data(service.actualizar(id, dto, token))
                        .build()
                );
        }

    @Operation(
            summary = "Eliminar evaluación",
            description = "Elimina una evaluación mediante su identificador"
        )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<ApiResponse<Void>> eliminar(

            @Parameter(
                    description = "ID de la evaluación",
                    example = "1"
            )
            @PathVariable Long id) {

        service.eliminar(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Evaluación eliminada")
                        .build()
                );
        }

    @Operation(
            summary = "Buscar evaluaciones por nombre",
            description = "Busca evaluaciones utilizando su nombre"
        )
    @GetMapping("/nombre/{nombre}")
    @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
    public ResponseEntity<ApiResponse<List<EvaluacionResponse>>> buscarPorNombre(

            @Parameter(
                    description = "Nombre de la evaluación",
                    example = "Prueba 1"
            )
            @PathVariable String nombre,

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<EvaluacionResponse>>builder()
                        .success(true)
                        .message("Evaluaciones encontradas por nombre")
                        .data(service.buscarPorNombre(nombre, token))
                        .build()
                );
        }

    @Operation(
            summary = "Buscar evaluaciones por curso",
            description = "Busca evaluaciones utilizando el ID del curso"
        )
    @GetMapping("/curso/{idCurso}")
    @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
    public ResponseEntity<ApiResponse<List<EvaluacionResponse>>> buscarPorCurso(

            @Parameter(
                    description = "ID del curso",
                    example = "1"
                )
            @PathVariable Long idCurso,

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<EvaluacionResponse>>builder()
                        .success(true)
                        .message("Evaluaciones encontradas por curso")
                        .data(service.buscarPorCurso(idCurso, token))
                        .build()
                );
        }
}