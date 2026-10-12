package com.campusstudy.grupos.config;

import com.campusstudy.grupos.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class AuthFilter extends OncePerRequestFilter {

    public static final String ATTR_USUARIO_ID = "usuarioId";

    private final ObjectMapper objectMapper;
    private final boolean demoMode;

    public AuthFilter(ObjectMapper objectMapper,
                      @Value("${campusstudy.auth.demo-mode:true}") boolean demoMode) {
        this.objectMapper = objectMapper;
        this.demoMode = demoMode;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return !path.startsWith("/api/v1/grupos");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String auth = request.getHeader("Authorization");
        if (auth == null || auth.isBlank() || !auth.regionMatches(true, 0, "Bearer ", 0, 7)) {
            writeUnauthorized(request, response);
            return;
        }

        String token = auth.substring(7).trim();
        if (token.isEmpty()) {
            writeUnauthorized(request, response);
            return;
        }

        Long usuarioId;
        if (demoMode) {
            try {
                usuarioId = Long.parseLong(token);
            } catch (NumberFormatException ex) {
                writeUnauthorized(request, response);
                return;
            }
        } else {
            writeUnauthorized(request, response);
            return;
        }

        if (usuarioId <= 0) {
            writeUnauthorized(request, response);
            return;
        }

        request.setAttribute(ATTR_USUARIO_ID, usuarioId);
        filterChain.doFilter(request, response);
    }

    private void writeUnauthorized(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponse body = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                "Token ausente, caducado o inválido",
                request.getRequestURI()
        );
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
