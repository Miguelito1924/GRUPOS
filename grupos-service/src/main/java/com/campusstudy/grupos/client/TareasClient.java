package com.campusstudy.grupos.client;

import com.campusstudy.grupos.dto.EventoCalendarioResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class TareasClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final boolean stubsEnabled;

    public TareasClient(
            RestTemplate restTemplate,
            @Value("${campusstudy.clients.tareas-base-url}") String baseUrl,
            @Value("${campusstudy.stubs.enabled:true}") boolean stubsEnabled) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.stubsEnabled = stubsEnabled;
    }

    public List<EventoCalendarioResponse> obtenerEventosGrupo(Long grupoId, String nombreGrupo, String nombreMateria) {
        if (stubsEnabled) {
            EventoCalendarioResponse evento = new EventoCalendarioResponse();
            evento.setTarea_id(15L);
            evento.setTitulo_trabajo("Contrato de Interfaz");
            evento.setDescripcion("Entrega consolidada de microservicios");
            evento.setNombre_materia(nombreMateria != null ? nombreMateria : "Sistemas Distribuidos");
            evento.setNombre_grupo(nombreGrupo);
            evento.setFecha_entrega("2026-09-30");
            return List.of(evento);
        }

        try {
            String url = baseUrl + "/api/v1/tareas?grupo_id=" + grupoId;
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );
            if (response.getBody() == null) {
                return Collections.emptyList();
            }
            return response.getBody().stream().map(item -> {
                EventoCalendarioResponse e = new EventoCalendarioResponse();
                e.setTarea_id(asLong(item.get("tarea_id")));
                e.setTitulo_trabajo(asString(item.getOrDefault("titulo_trabajo", item.get("titulo"))));
                e.setDescripcion(asString(item.get("descripcion")));
                e.setNombre_materia(nombreMateria);
                e.setNombre_grupo(nombreGrupo);
                e.setFecha_entrega(asString(item.get("fecha_entrega")));
                return e;
            }).toList();
        } catch (RestClientException ex) {
            // Degradación: detalle del grupo sin calendario externo
            return Collections.emptyList();
        }
    }

    private Long asLong(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number n) {
            return n.longValue();
        }
        return Long.parseLong(value.toString());
    }

    private String asString(Object value) {
        return value == null ? null : value.toString();
    }
}
