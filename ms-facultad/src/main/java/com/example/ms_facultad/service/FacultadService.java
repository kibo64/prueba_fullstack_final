package com.example.ms_facultad.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ms_facultad.dto.FacultadDTO;
import com.example.ms_facultad.model.Facultad;
import com.example.ms_facultad.repository.FacultadRepository;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Service
@RequiredArgsConstructor
@Slf4j
public class FacultadService {

    private final FacultadRepository repo;

    public Facultad crear(FacultadDTO dto) {
        log.info("Crear facultad", keyValue("nombre", dto.getNombre()));

        Facultad f = new Facultad(null, dto.getNombre(), dto.getUbicacion());
        return repo.save(f);
    }

    public List<Facultad> listar() {
        log.info("Listar facultades");
        return repo.findAll();
    }

    public Facultad obtener(Long id) {
        log.info("Obtener facultad", keyValue("id", id));

        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Facultad no encontrada"));
    }

    public Facultad actualizar(Long id, FacultadDTO dto) {
        log.info("Actualizar facultad", keyValue("id", id));

        Facultad f = obtener(id);
        f.setNombre(dto.getNombre());
        f.setUbicacion(dto.getUbicacion());

        return repo.save(f);
    }

    public List<Facultad> buscarPorNombre(String nombre) {
        log.info("Buscar facultad por nombre", keyValue("nombre", nombre));
        return repo.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Facultad> buscarPorUbicacion(String ubicacion) {
        log.info("Buscar facultad por ubicacion", keyValue("ubicacion", ubicacion));
        return repo.findByUbicacionContainingIgnoreCase(ubicacion);
    }
    public void eliminar(Long id) {
        log.warn("Eliminar facultad", keyValue("id", id));
        repo.deleteById(id);
    }
}