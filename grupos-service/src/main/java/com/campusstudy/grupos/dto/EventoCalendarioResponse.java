package com.campusstudy.grupos.dto;

public class EventoCalendarioResponse {
    private Long tarea_id;
    private String titulo_trabajo;
    private String descripcion;
    private String nombre_materia;
    private String nombre_grupo;
    private String fecha_entrega;

    public Long getTarea_id() {
        return tarea_id;
    }

    public void setTarea_id(Long tarea_id) {
        this.tarea_id = tarea_id;
    }

    public String getTitulo_trabajo() {
        return titulo_trabajo;
    }

    public void setTitulo_trabajo(String titulo_trabajo) {
        this.titulo_trabajo = titulo_trabajo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre_materia() {
        return nombre_materia;
    }

    public void setNombre_materia(String nombre_materia) {
        this.nombre_materia = nombre_materia;
    }

    public String getNombre_grupo() {
        return nombre_grupo;
    }

    public void setNombre_grupo(String nombre_grupo) {
        this.nombre_grupo = nombre_grupo;
    }

    public String getFecha_entrega() {
        return fecha_entrega;
    }

    public void setFecha_entrega(String fecha_entrega) {
        this.fecha_entrega = fecha_entrega;
    }
}
