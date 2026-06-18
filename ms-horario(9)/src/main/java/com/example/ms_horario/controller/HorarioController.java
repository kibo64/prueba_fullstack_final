package com.example.ms_horario.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.ms_horario.dto.ApiResponse;
import com.example.ms_horario.dto.HorarioDTO;
import com.example.ms_horario.dto.HorarioResponse;
import com.example.ms_horario.service.HorarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
        name = "Horarios",
        description = "Lista de acciones con horarios"
)
@RestController
@RequestMapping("/api/v1/horarios")
@RequiredArgsConstructor
public class HorarioController {

    private final HorarioService service;

    @Operation(
            summary = "Crear horario",
            description = "Permite crear un nuevo horario"
    )
    @PostMapping
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<ApiResponse<HorarioResponse>> crear(
            @Valid @RequestBody HorarioDTO dto,

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.status(201).body(
                ApiResponse.<HorarioResponse>builder()
                        .success(true)
                        .message("Horario creado")
                        .data(service.crear(dto, token))
                        .build()
        );
    }

    @Operation(
            summary = "Listar horarios",
            description = "Obtiene todos los horarios registrados"
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
    public ResponseEntity<ApiResponse<List<HorarioResponse>>> listar(

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<HorarioResponse>>builder()
                        .success(true)
                        .message("Listado obtenido")
                        .data(service.listar(token))
                        .build()
        );
    }

    @Operation(
            summary = "Buscar horario por ID",
            description = "Obtiene un horario según su identificador"
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
    public ResponseEntity<EntityModel<ApiResponse<HorarioResponse>>> obtener(

            @Parameter(
                    description = "ID del horario",
                    example = "1"
            )
            @PathVariable Long id,

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        HorarioResponse horario = service.obtener(id, token);

        ApiResponse<HorarioResponse> response =
                ApiResponse.<HorarioResponse>builder()
                        .success(true)
                        .message("Horario obtenido. Si posee rol PROFESOR también puede utilizar los métodos de actualizar y eliminar este recurso.")
                        .data(horario)
                        .build();

        EntityModel<ApiResponse<HorarioResponse>> recurso =
                EntityModel.of(response);

        recurso.add(
                linkTo(
                        methodOn(HorarioController.class)
                                .obtener(id, null)
                ).withSelfRel()
        );

        recurso.add(
                linkTo(
                        methodOn(HorarioController.class)
                                .listar(null)
                ).withRel("all")
        );

        recurso.add(
                linkTo(
                        methodOn(HorarioController.class)
                                .buscarPorCurso(
                                        horario.getCurso().getId(),
                                        null
                                )
                ).withRel("buscar-por-curso")
        );

        recurso.add(
                linkTo(
                        methodOn(HorarioController.class)
                                .buscarPorDia(
                                        horario.getDia(),
                                        null
                                )
                ).withRel("buscar-por-dia")
        );

        recurso.add(
                linkTo(
                        methodOn(HorarioController.class)
                                .actualizar(id, null, null)
                ).withRel("update (requiere rol PROFESOR)")
        );

        recurso.add(
                linkTo(
                        methodOn(HorarioController.class)
                                .eliminar(id)
                ).withRel("delete (requiere rol PROFESOR)")
        );

        return ResponseEntity.ok(recurso);
    }

    @Operation(
            summary = "Actualizar horario",
            description = "Actualiza un horario existente"
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<ApiResponse<HorarioResponse>> actualizar(

            @Parameter(
                    description = "ID del horario",
                    example = "1"
            )
            @PathVariable Long id,

            @Valid @RequestBody HorarioDTO dto,

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<HorarioResponse>builder()
                        .success(true)
                        .message("Horario actualizado")
                        .data(service.actualizar(id, dto, token))
                        .build()
        );
    }

    @Operation(
            summary = "Eliminar horario",
            description = "Elimina un horario mediante su identificador"
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<ApiResponse<Void>> eliminar(

            @Parameter(
                    description = "ID del horario",
                    example = "1"
            )
            @PathVariable Long id) {

        service.eliminar(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Horario eliminado")
                        .build()
        );
    }

    @Operation(
            summary = "Buscar horarios por curso",
            description = "Busca horarios utilizando el ID del curso"
    )
    @GetMapping("/curso/{idCurso}")
    @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
    public ResponseEntity<ApiResponse<List<HorarioResponse>>> buscarPorCurso(

            @Parameter(
                    description = "ID del curso",
                    example = "1"
            )
            @PathVariable Long idCurso,

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<HorarioResponse>>builder()
                        .success(true)
                        .message("Horarios encontrados por curso")
                        .data(service.buscarPorCurso(idCurso, token))
                        .build()
        );
    }

    @Operation(
            summary = "Buscar horarios por día",
            description = "Busca horarios utilizando el día"
    )
    @GetMapping("/dia/{dia}")
    @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
    public ResponseEntity<ApiResponse<List<HorarioResponse>>> buscarPorDia(

            @Parameter(
                    description = "Día del horario",
                    example = "Lunes"
            )
            @PathVariable String dia,

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<HorarioResponse>>builder()
                        .success(true)
                        .message("Horarios encontrados por día")
                        .data(service.buscarPorDia(dia, token))
                        .build()
        );
    }
}