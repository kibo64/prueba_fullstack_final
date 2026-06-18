package com.example.ms_inscripciones.controller;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.ms_inscripciones.dto.ApiResponse;
import com.example.ms_inscripciones.dto.InscripcionDTO;
import com.example.ms_inscripciones.dto.InscripcionResponse;
import com.example.ms_inscripciones.service.InscripcionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(
        name = "Inscripciones",
        description = "Lista de acciones con inscripciones"
)
@RestController
@RequestMapping("/api/v1/inscripciones")
@RequiredArgsConstructor
public class InscripcionController {

    private final InscripcionService service;

    @Operation(
            summary = "Crear inscripción",
            description = "Permite crear una nueva inscripción"
    )
    @PostMapping
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<ApiResponse<InscripcionResponse>> crear(

            @Valid @RequestBody InscripcionDTO dto,

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.status(201).body(
                ApiResponse.<InscripcionResponse>builder()
                        .success(true)
                        .message("Inscripción creada")
                        .data(service.crear(dto, token))
                        .build()
        );
    }

    @Operation(
            summary = "Listar inscripciones",
            description = "Obtiene todas las inscripciones registradas"
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
    public ResponseEntity<ApiResponse<List<InscripcionResponse>>> listar(

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<InscripcionResponse>>builder()
                        .success(true)
                        .message("Listado obtenido")
                        .data(service.listar(token))
                        .build()
        );
    }

    @Operation(
            summary = "Buscar inscripción por ID",
            description = "Obtiene una inscripción según su identificador"
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
    public ResponseEntity<EntityModel<ApiResponse<InscripcionResponse>>> obtener(

            @Parameter(
                    description = "ID de la inscripción",
                    example = "1"
            )
            @PathVariable Long id,

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        InscripcionResponse inscripcion = service.obtener(id, token);

        ApiResponse<InscripcionResponse> response =
                ApiResponse.<InscripcionResponse>builder()
                        .success(true)
                        .message("Inscripción obtenida. Si posee rol PROFESOR también puede utilizar los métodos de actualizar y eliminar este recurso.")
                        .data(inscripcion)
                        .build();

        EntityModel<ApiResponse<InscripcionResponse>> recurso =
                EntityModel.of(response);

        recurso.add(
                linkTo(
                        methodOn(InscripcionController.class)
                                .obtener(id, null)
                ).withSelfRel()
        );

        recurso.add(
                linkTo(
                        methodOn(InscripcionController.class)
                                .listar(null)
                ).withRel("all")
        );

        recurso.add(
                linkTo(
                        methodOn(InscripcionController.class)
                                .buscarPorEstudiante(
                                        inscripcion.getEstudiante().getId(),
                                        null
                                )
                ).withRel("buscar-por-estudiante")
        );

        recurso.add(
                linkTo(
                        methodOn(InscripcionController.class)
                                .buscarPorCurso(
                                        inscripcion.getCurso().getId(),
                                        null
                                )
                ).withRel("buscar-por-curso")
        );

        recurso.add(
                linkTo(
                        methodOn(InscripcionController.class)
                                .actualizar(id, null, null)
                ).withRel("update (requiere rol PROFESOR)")
        );

        recurso.add(
                linkTo(
                        methodOn(InscripcionController.class)
                                .eliminar(id)
                ).withRel("delete (requiere rol PROFESOR)")
        );

        return ResponseEntity.ok(recurso);
    }

    @Operation(
            summary = "Actualizar inscripción",
            description = "Actualiza una inscripción existente"
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<ApiResponse<InscripcionResponse>> actualizar(

            @Parameter(
                    description = "ID de la inscripción",
                    example = "1"
            )
            @PathVariable Long id,

            @Valid @RequestBody InscripcionDTO dto,

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<InscripcionResponse>builder()
                        .success(true)
                        .message("Inscripción actualizada")
                        .data(service.actualizar(id, dto, token))
                        .build()
        );
    }

    @Operation(
            summary = "Eliminar inscripción",
            description = "Elimina una inscripción mediante su identificador"
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<ApiResponse<Void>> eliminar(

            @Parameter(
                    description = "ID de la inscripción",
                    example = "1"
            )
            @PathVariable Long id) {

        service.eliminar(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Inscripción eliminada")
                        .build()
        );
    }

    @Operation(
            summary = "Buscar inscripciones por estudiante",
            description = "Busca inscripciones utilizando el ID del estudiante"
    )
    @GetMapping("/estudiante/{idEstudiante}")
    @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
    public ResponseEntity<ApiResponse<List<InscripcionResponse>>> buscarPorEstudiante(

            @Parameter(
                    description = "ID del estudiante",
                    example = "1"
            )
            @PathVariable Long idEstudiante,

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<InscripcionResponse>>builder()
                        .success(true)
                        .message("Inscripciones encontradas por estudiante")
                        .data(service.buscarPorEstudiante(idEstudiante, token))
                        .build()
        );
    }

    @Operation(
            summary = "Buscar inscripciones por curso",
            description = "Busca inscripciones utilizando el ID del curso"
    )
    @GetMapping("/curso/{idCurso}")
    @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
    public ResponseEntity<ApiResponse<List<InscripcionResponse>>> buscarPorCurso(

            @Parameter(
                    description = "ID del curso",
                    example = "1"
            )
            @PathVariable Long idCurso,

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<InscripcionResponse>>builder()
                        .success(true)
                        .message("Inscripciones encontradas por curso")
                        .data(service.buscarPorCurso(idCurso, token))
                        .build()
        );
    }
}
