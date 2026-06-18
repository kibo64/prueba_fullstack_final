package com.example.ms_asistencia.controller;

import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.ms_asistencia.dto.ApiResponse;
import com.example.ms_asistencia.dto.AsistenciaDTO;
import com.example.ms_asistencia.dto.AsistenciaResponse;
import com.example.ms_asistencia.service.AsistenciaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(
        name = "Asistencias",
        description = "Lista de acciones con asistencias"
)
@RestController
@RequestMapping("/api/v1/asistencias")
@RequiredArgsConstructor
public class AsistenciaController {

    private final AsistenciaService service;

    @Operation(
            summary = "Crear asistencia",
            description = "Permite registrar una asistencia"
    )
    @PostMapping
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<ApiResponse<AsistenciaResponse>> crear(
            @Valid @RequestBody AsistenciaDTO dto,

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.status(201).body(
                ApiResponse.<AsistenciaResponse>builder()
                        .success(true)
                        .message("Asistencia creada")
                        .data(service.crear(dto, token))
                        .build()
        );
    }

    @Operation(
            summary = "Listar asistencias",
            description = "Obtiene todas las asistencias registradas"
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
    public ResponseEntity<ApiResponse<List<AsistenciaResponse>>> listar(

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<AsistenciaResponse>>builder()
                        .success(true)
                        .message("Listado obtenido")
                        .data(service.listar(token))
                        .build()
        );
    }

    @Operation(
            summary = "Buscar asistencia por ID",
            description = "Obtiene una asistencia según su identificador"
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
    public ResponseEntity<EntityModel<ApiResponse<AsistenciaResponse>>> obtener(

            @Parameter(
                    description = "ID de la asistencia",
                    example = "1"
            )
            @PathVariable Long id,

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        AsistenciaResponse asistencia = service.obtener(id, token);

        ApiResponse<AsistenciaResponse> response =
                ApiResponse.<AsistenciaResponse>builder()
                        .success(true)
                        .message("Asistencia obtenida. Si posee rol PROFESOR también puede utilizar los métodos de actualizar y eliminar este recurso.")
                        .data(asistencia)
                        .build();

        EntityModel<ApiResponse<AsistenciaResponse>> recurso =
                EntityModel.of(response);

        recurso.add(
                linkTo(
                        methodOn(AsistenciaController.class)
                                .obtener(id, null)
                ).withSelfRel()
        );

        recurso.add(
                linkTo(
                        methodOn(AsistenciaController.class)
                                .listar(null)
                ).withRel("all")
        );

        recurso.add(
                linkTo(
                        methodOn(AsistenciaController.class)
                                .buscarPorEstudiante(
                                        asistencia.getEstudiante().getId(),
                                        null
                                )
                ).withRel("buscar-por-estudiante")
        );

        recurso.add(
                linkTo(
                        methodOn(AsistenciaController.class)
                                .buscarPorCurso(
                                        asistencia.getCurso().getId(),
                                        null
                                )
                ).withRel("buscar-por-curso")
        );

        recurso.add(
                linkTo(
                        methodOn(AsistenciaController.class)
                                .porcentajeAsistencia(
                                        asistencia.getEstudiante().getId(),
                                        asistencia.getCurso().getId()
                                )
                ).withRel("porcentaje-asistencia")
        );

        recurso.add(
                linkTo(
                        methodOn(AsistenciaController.class)
                                .actualizar(id, null, null)
                ).withRel("update (requiere rol PROFESOR)")
        );

        recurso.add(
                linkTo(
                        methodOn(AsistenciaController.class)
                                .eliminar(id)
                ).withRel("delete (requiere rol PROFESOR)")
        );

        return ResponseEntity.ok(recurso);
    }

    @Operation(
            summary = "Actualizar asistencia",
            description = "Actualiza una asistencia existente"
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<ApiResponse<AsistenciaResponse>> actualizar(

            @Parameter(
                    description = "ID de la asistencia",
                    example = "1"
            )
            @PathVariable Long id,

            @Valid @RequestBody AsistenciaDTO dto,

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<AsistenciaResponse>builder()
                        .success(true)
                        .message("Asistencia actualizada")
                        .data(service.actualizar(id, dto, token))
                        .build()
        );
    }

    @Operation(
            summary = "Eliminar asistencia",
            description = "Elimina una asistencia mediante su identificador"
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<ApiResponse<Void>> eliminar(

            @Parameter(
                    description = "ID de la asistencia",
                    example = "1"
            )
            @PathVariable Long id) {

        service.eliminar(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Asistencia eliminada")
                        .build()
        );
    }

    @Operation(
            summary = "Buscar asistencias por estudiante",
            description = "Busca asistencias utilizando el ID del estudiante"
    )
    @GetMapping("/estudiante/{idEstudiante}")
    @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
    public ResponseEntity<ApiResponse<List<AsistenciaResponse>>> buscarPorEstudiante(

            @Parameter(
                    description = "ID del estudiante",
                    example = "1"
            )
            @PathVariable Long idEstudiante,

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<AsistenciaResponse>>builder()
                        .success(true)
                        .message("Asistencias encontradas por estudiante")
                        .data(service.buscarPorEstudiante(idEstudiante, token))
                        .build()
        );
    }

    @Operation(
            summary = "Buscar asistencias por curso",
            description = "Busca asistencias utilizando el ID del curso"
    )
    @GetMapping("/curso/{idCurso}")
    @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
    public ResponseEntity<ApiResponse<List<AsistenciaResponse>>> buscarPorCurso(

            @Parameter(
                    description = "ID del curso",
                    example = "1"
            )
            @PathVariable Long idCurso,

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<AsistenciaResponse>>builder()
                        .success(true)
                        .message("Asistencias encontradas por curso")
                        .data(service.buscarPorCurso(idCurso, token))
                        .build()
        );
    }

    @Operation(
            summary = "Calcular porcentaje de asistencia",
            description = "Calcula el porcentaje de asistencia de un estudiante en un curso"
    )
    @GetMapping("/porcentaje/estudiante/{idEstudiante}/curso/{idCurso}")
    @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
    public ResponseEntity<ApiResponse<Double>> porcentajeAsistencia(

            @Parameter(
                    description = "ID del estudiante",
                    example = "1"
            )
            @PathVariable Long idEstudiante,

            @Parameter(
                    description = "ID del curso",
                    example = "1"
            )
            @PathVariable Long idCurso) {

        return ResponseEntity.ok(
                ApiResponse.<Double>builder()
                        .success(true)
                        .message("Porcentaje de asistencia calculado")
                        .data(service.porcentajeAsistencia(idEstudiante, idCurso))
                        .build()
        );
    }
}