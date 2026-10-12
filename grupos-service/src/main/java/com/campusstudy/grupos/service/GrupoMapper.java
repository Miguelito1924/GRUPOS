package com.campusstudy.grupos.service;

import com.campusstudy.grupos.dto.*;
import com.campusstudy.grupos.model.Documento;
import com.campusstudy.grupos.model.Grupo;
import com.campusstudy.grupos.model.Integrante;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Component
public class GrupoMapper {

    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public String format(LocalDateTime value) {
        return value == null ? null : value.format(DT);
    }

    public IntegranteResponse toIntegrante(Integrante integrante) {
        return new IntegranteResponse(
                integrante.getUsuarioId(),
                integrante.getRol().name(),
                format(integrante.getUnidoEn())
        );
    }

    public DocumentoResponse toDocumento(Documento documento) {
        DocumentoResponse dto = new DocumentoResponse();
        dto.setDocumento_id(documento.getId());
        dto.setGrupo_id(documento.getGrupo().getId());
        dto.setNombre(documento.getNombre());
        dto.setTipo(documento.getTipo().name());
        dto.setUrl(documento.getUrl());
        dto.setFecha_actualizacion(format(documento.getFechaActualizacion()));
        return dto;
    }

    public GrupoListadoResponse toListado(Grupo grupo) {
        GrupoListadoResponse dto = new GrupoListadoResponse();
        dto.setGrupo_id(grupo.getId());
        dto.setNombre(grupo.getNombre());
        dto.setMateria_id(grupo.getMateriaId());
        dto.setCreado_por(grupo.getCreadoPor());
        dto.setIntegrantes(grupo.getIntegrantes().stream().map(this::toIntegrante).toList());
        dto.setFecha_creacion(format(grupo.getFechaCreacion()));
        return dto;
    }

    public GrupoDetalleResponse toDetalle(
            Grupo grupo,
            String nombreMateria,
            List<EventoCalendarioResponse> eventos) {

        GrupoDetalleResponse dto = new GrupoDetalleResponse();
        dto.setGrupo_id(grupo.getId());
        dto.setNombre(grupo.getNombre());
        dto.setMateria_id(grupo.getMateriaId());
        dto.setNombre_materia(nombreMateria);
        dto.setCreado_por(grupo.getCreadoPor());
        dto.setHerramientas(new HerramientasResponse(grupo.getZoomUrl(), grupo.getJamUrl()));
        dto.setDocumentos(grupo.getDocumentos().stream()
                .sorted(Comparator.comparing(Documento::getFechaActualizacion).reversed())
                .map(this::toDocumento)
                .toList());
        dto.setCalendario_eventos(eventos);
        dto.setIntegrantes(grupo.getIntegrantes().stream().map(this::toIntegrante).toList());
        dto.setFecha_creacion(format(grupo.getFechaCreacion()));
        return dto;
    }

    public GrupoDetalleResponse toCreacion(Grupo grupo) {
        GrupoDetalleResponse dto = new GrupoDetalleResponse();
        dto.setGrupo_id(grupo.getId());
        dto.setNombre(grupo.getNombre());
        dto.setMateria_id(grupo.getMateriaId());
        dto.setCreado_por(grupo.getCreadoPor());
        dto.setHerramientas(new HerramientasResponse(grupo.getZoomUrl(), grupo.getJamUrl()));
        dto.setIntegrantes(grupo.getIntegrantes().stream().map(this::toIntegrante).toList());
        dto.setFecha_creacion(format(grupo.getFechaCreacion()));
        return dto;
    }
}
