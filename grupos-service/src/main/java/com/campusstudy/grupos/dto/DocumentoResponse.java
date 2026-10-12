package com.campusstudy.grupos.dto;

public class DocumentoResponse {
    private Long documento_id;
    private Long grupo_id;
    private String nombre;
    private String tipo;
    private String url;
    private String fecha_actualizacion;

    public Long getDocumento_id() {
        return documento_id;
    }

    public void setDocumento_id(Long documento_id) {
        this.documento_id = documento_id;
    }

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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFecha_actualizacion() {
        return fecha_actualizacion;
    }

    public void setFecha_actualizacion(String fecha_actualizacion) {
        this.fecha_actualizacion = fecha_actualizacion;
    }
}
