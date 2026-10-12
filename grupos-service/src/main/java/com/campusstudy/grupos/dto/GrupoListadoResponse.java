package com.campusstudy.grupos.dto;

import java.util.List;

public class GrupoListadoResponse {
    private Long grupo_id;
    private String nombre;
    private Long materia_id;
    private Long creado_por;
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

    public Long getCreado_por() {
        return creado_por;
    }

    public void setCreado_por(Long creado_por) {
        this.creado_por = creado_por;
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
