package com.campusstudy.grupos.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class UsuariosClient {

    private final RestTemplate restTemplate;
    private final RetrySupport retrySupport;
    private final String baseUrl;
    private final boolean stubsEnabled;

    public UsuariosClient(
            RestTemplate restTemplate,
            RetrySupport retrySupport,
            @Value("${campusstudy.clients.usuarios-base-url}") String baseUrl,
            @Value("${campusstudy.stubs.enabled:true}") boolean stubsEnabled) {
        this.restTemplate = restTemplate;
        this.retrySupport = retrySupport;
        this.baseUrl = baseUrl;
        this.stubsEnabled = stubsEnabled;
    }

    public boolean existeUsuario(Long usuarioId) {
        if (stubsEnabled) {
            if (usuarioId != null && usuarioId == 999L) {
                return false;
            }
            if (usuarioId != null && usuarioId == 998L) {
                throw new DependencyUnavailableException("usuarios-service",
                        "usuarios-service no disponible (stub 503)");
            }
            return usuarioId != null && usuarioId > 0;
        }

        return Boolean.TRUE.equals(retrySupport.execute("usuarios-service", () -> {
            try {
                String url = baseUrl + "/api/v1/usuarios/" + usuarioId;
                ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
                return response.getStatusCode().is2xxSuccessful();
            } catch (HttpStatusCodeException ex) {
                HttpStatusCode code = ex.getStatusCode();
                if (code.value() == 404) {
                    return false;
                }
                if (code.value() == 503) {
                    throw new DependencyUnavailableException("usuarios-service",
                            "usuarios-service respondió 503");
                }
                throw new DependencyUnavailableException("usuarios-service",
                        "Error al consultar usuarios-service: " + code.value());
            } catch (RestClientException ex) {
                throw new RuntimeException(ex);
            }
        }));
    }
}
