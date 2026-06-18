package com.example.ms_facultad.controller;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_facultad.dto.ApiResponse;
import com.example.ms_facultad.dto.FacultadDTO;
import com.example.ms_facultad.model.Facultad;
import com.example.ms_facultad.service.FacultadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(
        name = "Facultades",
        description = " Lista de acciones con facultades"
)


@RestController
@RequestMapping("/api/v1/facultades")
@RequiredArgsConstructor
public class FacultadController {

    private final FacultadService service;

    @Operation(
            summary = "Crear facultad",
            description = "Permite crear una nueva facultad"
    )
    @PostMapping
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<ApiResponse<Facultad>> crear(
            @Valid @RequestBody FacultadDTO dto) {

        Facultad facultad = service.crear(dto);

        return ResponseEntity.status(201).body(
                ApiResponse.<Facultad>builder()
                        .success(true)
                        .message("Facultad creada")
                        .data(facultad)
                        .build()
        );
    }

    @Operation(
            summary = "Listar facultades",
            description = "Obtiene todas las facultades registradas"
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
    public ResponseEntity<ApiResponse<List<Facultad>>> listar() {

        return ResponseEntity.ok(
                ApiResponse.<List<Facultad>>builder()
                        .success(true)
                        .message("Listado obtenido")
                        .data(service.listar())
                        .build()
        );
    }

        @Operation(
                summary = "Buscar facultad por ID",
                description = "Obtiene una facultad según su identificador"
        )
        @GetMapping("/{id}")
        @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
        public ResponseEntity<EntityModel<ApiResponse<Facultad>>> obtener(

                @Parameter(
                        description = "ID de la facultad",
                        example = "1"
                )
                @PathVariable Long id) {

        ApiResponse<Facultad> response =
                ApiResponse.<Facultad>builder()
                        .success(true)
                        .message("Facultad obtenida. Si posee rol PROFESOR también puede utilizar los métodos de actualizar y eliminar este recurso.")
                        .data(service.obtener(id))
                        .build();

        EntityModel<ApiResponse<Facultad>> recurso =
                EntityModel.of(response);

        recurso.add(
                linkTo(
                        methodOn(FacultadController.class)
                                .obtener(id)
                ).withSelfRel()
        );

        recurso.add(
                linkTo(
                        methodOn(FacultadController.class)
                                .listar()
                ).withRel("all")
        );

        recurso.add(
                linkTo(
                        methodOn(FacultadController.class)
                                .buscarPorNombre(
                                        service.obtener(id).getNombre()
                                )
                ).withRel("buscar-por-nombre")
        );

        recurso.add(
                linkTo(
                        methodOn(FacultadController.class)
                                .buscarPorUbicacion(
                                        service.obtener(id).getUbicacion()
                                )
                ).withRel("buscar-por-ubicacion")
        );

        recurso.add(
                linkTo(
                        methodOn(FacultadController.class)
                                .actualizar(id, null)
                ).withRel("update (requiere rol PROFESOR)")
        );

        recurso.add(
                linkTo(
                        methodOn(FacultadController.class)
                                .eliminar(id)
                ).withRel("delete (requiere rol PROFESOR)")
        );

        return ResponseEntity.ok(recurso);
        }

    @Operation(
            summary = "Actualizar facultad",
            description = "Actualiza una facultad existente"
        )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<ApiResponse<Facultad>> actualizar(

            @Parameter(
                    description = "ID de la facultad",
                    example = "1"
            )
            @PathVariable Long id,

            @Valid @RequestBody FacultadDTO dto) {

        Facultad facultad = service.actualizar(id, dto);

        return ResponseEntity.ok(
                ApiResponse.<Facultad>builder()
                        .success(true)
                        .message("Facultad actualizada")
                        .data(facultad)
                        .build()
        );
    }

    @Operation(
            summary = "Buscar facultades por nombre",
            description = "Busca facultades utilizando su nombre"
    )
    @GetMapping("/nombre/{nombre}")
    @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
    public ResponseEntity<ApiResponse<List<Facultad>>> buscarPorNombre(

            @Parameter(
                    description = "Nombre de la facultad",
                    example = "Ingeniería"
            )
            @PathVariable String nombre) {

        return ResponseEntity.ok(
                ApiResponse.<List<Facultad>>builder()
                        .success(true)
                        .message("Facultades encontradas por nombre")
                        .data(service.buscarPorNombre(nombre))
                        .build()
        );
    }

    @Operation(
            summary = "Buscar facultades por ubicación",
            description = "Busca facultades utilizando la ubicación"
    )
    @GetMapping("/ubicacion/{ubicacion}")
    @PreAuthorize("hasAnyRole('ESTUDIANTE','PROFESOR')")
    public ResponseEntity<ApiResponse<List<Facultad>>> buscarPorUbicacion(

            @Parameter(
                    description = "Ubicación de la facultad",
                    example = "Campus Norte"
            )
            @PathVariable String ubicacion) {

        return ResponseEntity.ok(
                ApiResponse.<List<Facultad>>builder()
                        .success(true)
                        .message("Facultades encontradas por ubicación")
                        .data(service.buscarPorUbicacion(ubicacion))
                        .build()
        );
    }

    @Operation(
            summary = "Eliminar facultad",
            description = "Elimina una facultad mediante su identificador"
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<ApiResponse<Void>> eliminar(

            @Parameter(
                    description = "ID de la facultad",
                    example = "1"
            )
            @PathVariable Long id) {

        service.eliminar(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Facultad eliminada")
                        .build()
        );
    }
}