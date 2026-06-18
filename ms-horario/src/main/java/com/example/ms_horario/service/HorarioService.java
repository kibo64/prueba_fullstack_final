package com.example.ms_horario.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ms_horario.client.CursoClient;
import com.example.ms_horario.dto.HorarioDTO;
import com.example.ms_horario.dto.HorarioResponse;
import com.example.ms_horario.model.Horario;
import com.example.ms_horario.repository.HorarioRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class HorarioService {

    private final HorarioRepository repo;
    private final CursoClient cursoClient;

    public HorarioResponse crear(HorarioDTO dto, String token) {

        log.info("Creando horario para curso {} el día {}", dto.getIdCurso(), dto.getDia());

        try {
            cursoClient.obtenerCurso(dto.getIdCurso(), token);
        } catch (Exception e) {
            log.error("Curso {} no existe", dto.getIdCurso());
            throw new RuntimeException("Curso no existe");
        }

        Horario horario = repo.save(
                new Horario(
                        null,
                        dto.getDia(),
                        dto.getHoraInicio(),
                        dto.getHoraFin(),
                        dto.getSala(),
                        dto.getIdCurso()
                )
        );

        log.info("Horario creado con id {}", horario.getId());

        return mapToResponse(horario, token);
    }

    public List<HorarioResponse> listar(String token) {

        log.info("Listando horarios");

        return repo.findAll()
                .stream()
                .map(h -> mapToResponse(h, token))
                .toList();
    }

    public HorarioResponse obtener(Long id, String token) {

        log.info("Buscando horario con id {}", id);

        Horario horario = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Horario no encontrado"));

        return mapToResponse(horario, token);
    }

    public HorarioResponse actualizar(Long id, HorarioDTO dto, String token) {

        log.info("Actualizando horario con id {}", id);

        Horario h = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Horario no encontrado"));

        try {
            cursoClient.obtenerCurso(dto.getIdCurso(), token);
        } catch (Exception e) {
            log.error("Curso {} no existe", dto.getIdCurso());
            throw new RuntimeException("Curso no existe");
        }

        h.setDia(dto.getDia());
        h.setHoraInicio(dto.getHoraInicio());
        h.setHoraFin(dto.getHoraFin());
        h.setSala(dto.getSala());
        h.setIdCurso(dto.getIdCurso());

        log.info("Horario actualizado con id {}", id);

        return mapToResponse(repo.save(h), token);
    }

    public void eliminar(Long id) {

        log.info("Eliminando horario con id {}", id);

        repo.deleteById(id);
    }

    public List<HorarioResponse> buscarPorCurso(Long idCurso, String token) {

        log.info("Buscando horarios por curso {}", idCurso);

        return repo.findByIdCurso(idCurso)
                .stream()
                .map(h -> mapToResponse(h, token))
                .toList();
    }

    public List<HorarioResponse> buscarPorDia(String dia, String token) {

        log.info("Buscando horarios por día {}", dia);

        return repo.findByDiaIgnoreCase(dia)
                .stream()
                .map(h -> mapToResponse(h, token))
                .toList();
    }

    private HorarioResponse mapToResponse(Horario horario, String token) {

        log.debug("Mapeando horario {} a response", horario.getId());

        var curso = cursoClient.obtenerCurso(horario.getIdCurso(), token);

        return HorarioResponse.builder()
                .id(horario.getId())
                .dia(horario.getDia())
                .horaInicio(horario.getHoraInicio())
                .horaFin(horario.getHoraFin())
                .sala(horario.getSala())
                .curso(curso)
                .build();
    }
}