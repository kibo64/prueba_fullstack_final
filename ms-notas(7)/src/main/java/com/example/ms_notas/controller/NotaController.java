package com.example.ms_notas.controller;
import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.ms_notas.dto.ApiResponse;
import com.example.ms_notas.dto.NotaDTO;
import com.example.ms_notas.dto.NotaResponse;
import com.example.ms_notas.service.NotaService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
        name = "Notas",
        description = "Lista de acciones con notas"
)
@RestController
@RequestMapping("/api/v1/notas")
@RequiredArgsConstructor
public class NotaController {

    private final NotaService service;

        @Operation(
            summary = "Crear nota",
            description = "Permite crear o dar una nota a una evaluacion"
        )
    @PostMapping
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<ApiResponse<NotaResponse>> crear(
            @Valid @RequestBody NotaDTO dto,
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.status(201).body(
                ApiResponse.<NotaResponse>builder()
                        .success(true)
                        .message("Nota creada")
                        .data(service.crear(dto, token))
                        .build()
                );
        }

        @Operation(
                summary = "Listar notas",
                description = "Obtiene todas las notas registradas"
        )
    @GetMapping
    @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
    public ResponseEntity<ApiResponse<List<NotaResponse>>> listar(
                @Parameter(hidden = true)    
                @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<NotaResponse>>builder()
                        .success(true)
                        .message("Listado obtenido")
                        .data(service.listar(token))
                        .build()
                );
        }

        @Operation(
                summary = "Buscar nota por ID",
                description = "Obtiene una nota según su identificador"
        )
        @GetMapping("/{id}")
        @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
        public ResponseEntity<EntityModel<ApiResponse<NotaResponse>>> obtener(

                @Parameter(
                        description = "ID de la nota",
                        example = "1"
                )
                @PathVariable Long id,

                @Parameter(hidden = true)
                @RequestHeader("Authorization") String token) {

        NotaResponse nota = service.obtener(id, token);

        ApiResponse<NotaResponse> response =
                ApiResponse.<NotaResponse>builder()
                        .success(true)
                        .message("Nota obtenida. Si posee rol PROFESOR también puede utilizar los métodos de actualizar y eliminar este recurso.")
                        .data(nota)
                        .build();

        EntityModel<ApiResponse<NotaResponse>> recurso =
                EntityModel.of(response);

        recurso.add(
                linkTo(
                        methodOn(NotaController.class)
                                .obtener(id, null)
                ).withSelfRel()
        );

        recurso.add(
                linkTo(
                        methodOn(NotaController.class)
                                .listar(null)
                ).withRel("all")
        );

        recurso.add(
                linkTo(
                        methodOn(NotaController.class)
                                .buscarPorEstudiante(
                                        nota.getEstudiante().getId(),
                                        null
                                )
                ).withRel("buscar-por-estudiante")
        );

        recurso.add(
                linkTo(
                        methodOn(NotaController.class)
                                .buscarPorEvaluacion(
                                        nota.getEvaluacion().getId(),
                                        null
                                )
                ).withRel("buscar-por-evaluacion")
        );

        recurso.add(
                linkTo(
                        methodOn(NotaController.class)
                                .promedioPorEstudiante(
                                        nota.getEstudiante().getId()
                                )
                ).withRel("promedio-estudiante")
        );

        recurso.add(
                linkTo(
                        methodOn(NotaController.class)
                                .actualizar(id, null, null)
                ).withRel("update (requiere rol PROFESOR)")
        );

        recurso.add(
                linkTo(
                        methodOn(NotaController.class)
                                .eliminar(id)
                ).withRel("delete (requiere rol PROFESOR)")
        );

        return ResponseEntity.ok(recurso);
        }
        
        @Operation(
            summary = "Actualizar nota",
            description = "Actualiza una nota existente"
        )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<ApiResponse<NotaResponse>> actualizar(

                @Parameter(
                description = "ID de la evaluación",
                example = "1"
                )
            @PathVariable Long id,
            @Valid @RequestBody NotaDTO dto,
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<NotaResponse>builder()
                        .success(true)
                        .message("Nota actualizada")
                        .data(service.actualizar(id, dto, token))
                        .build()
                );
        }

    @Operation(
            summary = "Eliminar nota",
            description = "Elimina una nota mediante su identificador"
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
                        .message("Nota eliminada")
                        .build()
                );
        }

    @Operation(
            summary = "Buscar las notas por estudiantes",
            description = "Busca las notas utilizando el ID del estudiamte"
        )
    @GetMapping("/estudiante/{idEstudiante}")
    @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
    public ResponseEntity<ApiResponse<List<NotaResponse>>> buscarPorEstudiante(
            
            @Parameter(
                    description = "ID del estudiante",
                    example = "1"
                )        
            @PathVariable Long idEstudiante,
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<NotaResponse>>builder()
                        .success(true)
                        .message("Notas encontradas por estudiante")
                        .data(service.buscarPorEstudiante(idEstudiante, token))
                        .build()
                );
        }

    @Operation(
            summary = "Buscar las notas por evaluacion",
            description = "Busca las notas utilizando el ID de la evaluacion"
        )
    @GetMapping("/evaluacion/{idEvaluacion}")
    @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
    public ResponseEntity<ApiResponse<List<NotaResponse>>> buscarPorEvaluacion(

            @Parameter(
                    description = "ID de la evaluacion",
                    example = "1"
                )  
            @PathVariable Long idEvaluacion,
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<NotaResponse>>builder()
                        .success(true)
                        .message("Notas encontradas por evaluación")
                        .data(service.buscarPorEvaluacion(idEvaluacion, token))
                        .build()
                );
        }

        @Operation(
                summary = "Calcular promedio de estudiante",
                description = "Calcula el promedio de todas las notas de un estudiante"
        )
        @GetMapping("/Promedio/{idEstudiante}")
        @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
        public ResponseEntity<ApiResponse<Double>> promedioPorEstudiante(

                @Parameter(
                        description = "ID del estudiante",
                        example = "1"
                )
                @PathVariable Long idEstudiante) {

        return ResponseEntity.ok(
                ApiResponse.<Double>builder()
                        .success(true)
                        .message("Promedio calculado")
                        .data(service.promedioPorEstudiante(idEstudiante))
                        .build()
                );
        }
}