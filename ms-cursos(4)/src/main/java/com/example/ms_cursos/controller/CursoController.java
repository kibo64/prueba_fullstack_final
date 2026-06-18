package com.example.ms_cursos.controller;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.ms_cursos.dto.ApiResponse;
import com.example.ms_cursos.dto.CursoDTO;
import com.example.ms_cursos.dto.CursoResponse;
import com.example.ms_cursos.service.CursoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(
        name = "Cursos",
        description = "Lista de acciones con cursos"
)
@RestController
@RequestMapping("/api/v1/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService service;

    @Operation(
            summary = "Crear curso",
            description = "Permite crear un nuevo curso"
    )
    @PostMapping
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<ApiResponse<CursoResponse>> crear(

            @Valid @RequestBody CursoDTO dto,

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.status(201).body(
                ApiResponse.<CursoResponse>builder()
                        .success(true)
                        .message("Curso creado")
                        .data(service.crear(dto, token))
                        .build()
        );
    }

    @Operation(
            summary = "Listar cursos",
            description = "Obtiene todos los cursos registrados"
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
    public ResponseEntity<ApiResponse<List<CursoResponse>>> listar(

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<CursoResponse>>builder()
                        .success(true)
                        .message("Listado obtenido")
                        .data(service.listar(token))
                        .build()
        );
    }

    @Operation(
            summary = "Buscar curso por ID",
            description = "Obtiene un curso según su identificador"
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
    public ResponseEntity<EntityModel<ApiResponse<CursoResponse>>> obtener(

            @Parameter(
                    description = "ID del curso",
                    example = "1"
            )
            @PathVariable Long id,

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        CursoResponse curso = service.obtener(id, token);

        ApiResponse<CursoResponse> response =
                ApiResponse.<CursoResponse>builder()
                        .success(true)
                        .message("Curso obtenido. Si posee rol PROFESOR también puede utilizar los métodos de actualizar y eliminar este recurso.")
                        .data(curso)
                        .build();

        EntityModel<ApiResponse<CursoResponse>> recurso =
                EntityModel.of(response);

        recurso.add(
                linkTo(
                        methodOn(CursoController.class)
                                .obtener(id, null)
                ).withSelfRel()
        );

        recurso.add(
                linkTo(
                        methodOn(CursoController.class)
                                .listar(null)
                ).withRel("all")
        );

        recurso.add(
                linkTo(
                        methodOn(CursoController.class)
                                .buscarPorNombre(
                                        curso.getNombreC(),
                                        null
                                )
                ).withRel("buscar-por-nombre")
        );

        recurso.add(
                linkTo(
                        methodOn(CursoController.class)
                                .buscarPorProfesor(
                                        curso.getProfesor().getId(),
                                        null
                                )
                ).withRel("buscar-por-profesor")
        );

        recurso.add(
                linkTo(
                        methodOn(CursoController.class)
                                .buscarPorFacultad(
                                        curso.getFacultad().getId(),
                                        null
                                )
                ).withRel("buscar-por-facultad")
        );

        recurso.add(
                linkTo(
                        methodOn(CursoController.class)
                                .actualizar(id, null, null)
                ).withRel("update (requiere rol PROFESOR)")
        );

        recurso.add(
                linkTo(
                        methodOn(CursoController.class)
                                .eliminar(id)
                ).withRel("delete (requiere rol PROFESOR)")
        );

        return ResponseEntity.ok(recurso);
    }

    @Operation(
            summary = "Actualizar curso",
            description = "Actualiza un curso existente"
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<ApiResponse<CursoResponse>> actualizar(

            @Parameter(
                    description = "ID del curso",
                    example = "1"
            )
            @PathVariable Long id,

            @Valid @RequestBody CursoDTO dto,

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<CursoResponse>builder()
                        .success(true)
                        .message("Curso actualizado")
                        .data(service.actualizar(id, dto, token))
                        .build()
        );
    }

    @Operation(
            summary = "Eliminar curso",
            description = "Elimina un curso mediante su identificador"
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<ApiResponse<Void>> eliminar(

            @Parameter(
                    description = "ID del curso",
                    example = "1"
            )
            @PathVariable Long id) {

        service.eliminar(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Curso eliminado")
                        .build()
        );
    }

    @Operation(
            summary = "Buscar cursos por nombre",
            description = "Busca cursos utilizando su nombre"
    )
    @GetMapping("/nombre/{nombre}")
    @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
    public ResponseEntity<ApiResponse<List<CursoResponse>>> buscarPorNombre(

            @Parameter(
                    description = "Nombre del curso",
                    example = "Programacion"
            )
            @PathVariable String nombre,

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<CursoResponse>>builder()
                        .success(true)
                        .message("Cursos encontrados por nombre")
                        .data(service.buscarPorNombre(nombre, token))
                        .build()
        );
    }

    @Operation(
            summary = "Buscar cursos por profesor",
            description = "Busca cursos utilizando el ID del profesor"
    )
    @GetMapping("/profesor/{profesorId}")
    @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
    public ResponseEntity<ApiResponse<List<CursoResponse>>> buscarPorProfesor(

            @Parameter(
                    description = "ID del profesor",
                    example = "1"
            )
            @PathVariable Long profesorId,

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<CursoResponse>>builder()
                        .success(true)
                        .message("Cursos encontrados por profesor")
                        .data(service.buscarPorProfesor(profesorId, token))
                        .build()
        );
    }

    @Operation(
            summary = "Buscar cursos por facultad",
            description = "Busca cursos utilizando el ID de la facultad"
    )
    @GetMapping("/facultad/{facultadId}")
    @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
    public ResponseEntity<ApiResponse<List<CursoResponse>>> buscarPorFacultad(

            @Parameter(
                    description = "ID de la facultad",
                    example = "1"
            )
            @PathVariable Long facultadId,

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<CursoResponse>>builder()
                        .success(true)
                        .message("Cursos encontrados por facultad")
                        .data(service.buscarPorFacultad(facultadId, token))
                        .build()
        );
    }
}

