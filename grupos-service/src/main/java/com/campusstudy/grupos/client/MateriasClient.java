package com.campusstudy.grupos.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Component
public class MateriasClient {

    private final RestTemplate restTemplate;
    private final RetrySupport retrySupport;
    private final String baseUrl;
    private final boolean stubsEnabled;

    public MateriasClient(
            RestTemplate restTemplate,
            RetrySupport retrySupport,
            @Value("${campusstudy.clients.materias-base-url}") String baseUrl,
            @Value("${campusstudy.stubs.enabled:true}") boolean stubsEnabled) {
        this.restTemplate = restTemplate;
        this.retrySupport = retrySupport;
        this.baseUrl = baseUrl;
        this.stubsEnabled = stubsEnabled;
    }

    public Optional<String> obtenerNombreMateria(Long materiaId) {
        if (stubsEnabled) {
            if (materiaId != null && materiaId == 999L) {
                return Optional.empty();
            }
            if (materiaId != null && materiaId == 998L) {
                throw new DependencyUnavailableException("materias-service",
                        "materias-service no disponible (stub 503)");
            }
            return Optional.of(nombreStub(materiaId));
        }

        return retrySupport.execute("materias-service", () -> {
            try {
                String url = baseUrl + "/api/v1/materias/" + materiaId;
                ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    Object nombre = response.getBody().get("nombre");
                    if (nombre != null) {
                        return Optional.of(nombre.toString());
                    }
                    return Optional.of("Materia " + materiaId);
                }
                return Optional.empty();
            } catch (HttpStatusCodeException ex) {
                HttpStatusCode code = ex.getStatusCode();
                if (code.value() == 404) {
                    return Optional.empty();
                }
                if (code.value() == 503) {
                    throw new DependencyUnavailableException("materias-service",
                            "materias-service respondió 503");
                }
                throw new DependencyUnavailableException("materias-service",
                        "Error al consultar materias-service: " + code.value());
            } catch (RestClientException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private String nombreStub(Long materiaId) {
        if (materiaId != null && materiaId == 12L) {
            return "Sistemas Distribuidos";
        }
        if (materiaId != null && materiaId == 15L) {
            return "Cálculo Diferencial";
        }
        return "Materia " + materiaId;
    }
}
