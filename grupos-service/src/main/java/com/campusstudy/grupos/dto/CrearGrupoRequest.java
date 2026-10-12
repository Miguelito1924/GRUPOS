package com.campusstudy.grupos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CrearGrupoRequest {

    @NotBlank(message = "El nombre del grupo es obligatorio")
    private String nombre;

    @NotNull(message = "Debe asociarse a una materia válida")
    @Positive(message = "Debe asociarse a una materia válida")
    private Long materia_id;

    private String enlace_zoom;
    private String enlace_jam;

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

    public String getEnlace_zoom() {
        return enlace_zoom;
    }

    public void setEnlace_zoom(String enlace_zoom) {
        this.enlace_zoom = enlace_zoom;
    }

    public String getEnlace_jam() {
        return enlace_jam;
    }

    public void setEnlace_jam(String enlace_jam) {
        this.enlace_jam = enlace_jam;
    }
}
