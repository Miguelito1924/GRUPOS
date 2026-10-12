package com.campusstudy.grupos.dto;

import java.util.List;

public class GrupoDetalleResponse {
    private Long grupo_id;
    private String nombre;
    private Long materia_id;
    private String nombre_materia;
    private Long creado_por;
    private HerramientasResponse herramientas;
    private List<DocumentoResponse> documentos;
    private List<EventoCalendarioResponse> calendario_eventos;
    private List<IntegranteResponse> integrantes;
    private String fecha_creacion;

    public Long getGrupo_id() {
        return grupo_id;
    }

    public void setGrupo_id(Long grupo_id) {
        this.grupo_id = grupo_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getMateria_id() {
        return materia_id;
    }

    public void setMateria_id(Long materia_id) {
        this.materia_id = materia_id;
    }

    public String getNombre_materia() {
        return nombre_materia;
    }

    public void setNombre_materia(String nombre_materia) {
        this.nombre_materia = nombre_materia;
    }

    public Long getCreado_por() {
        return creado_por;
    }

    public void setCreado_por(Long creado_por) {
        this.creado_por = creado_por;
    }

    public HerramientasResponse getHerramientas() {
        return herramientas;
    }

    public void setHerramientas(HerramientasResponse herramientas) {
        this.herramientas = herramientas;
    }

    public List<DocumentoResponse> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<DocumentoResponse> documentos) {
        this.documentos = documentos;
    }

    public List<EventoCalendarioResponse> getCalendario_eventos() {
        return calendario_eventos;
    }

    public void setCalendario_eventos(List<EventoCalendarioResponse> calendario_eventos) {
        this.calendario_eventos = calendario_eventos;
    }

    public List<IntegranteResponse> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(List<IntegranteResponse> integrantes) {
        this.integrantes = integrantes;
    }

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }
}
