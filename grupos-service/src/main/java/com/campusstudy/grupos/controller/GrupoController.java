package com.campusstudy.grupos.controller;

import com.campusstudy.grupos.config.AuthFilter;
import com.campusstudy.grupos.dto.*;
import com.campusstudy.grupos.service.GrupoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/grupos")
public class GrupoController {

    private final GrupoService grupoService;

    public GrupoController(GrupoService grupoService) {
        this.grupoService = grupoService;
    }

    @PostMapping
    public ResponseEntity<GrupoDetalleResponse> crear(
            @Valid @RequestBody CrearGrupoRequest request,
            HttpServletRequest httpRequest) {
        Long usuarioId = (Long) httpRequest.getAttribute(AuthFilter.ATTR_USUARIO_ID);
        return ResponseEntity.status(HttpStatus.CREATED).body(grupoService.crearGrupo(request, usuarioId));
    }

    @GetMapping
    public ResponseEntity<List<GrupoListadoResponse>> listar(HttpServletRequest httpRequest) {
        Long usuarioId = (Long) httpRequest.getAttribute(AuthFilter.ATTR_USUARIO_ID);
        return ResponseEntity.ok(grupoService.listarMisGrupos(usuarioId));
    }

    @GetMapping("/resumen")
    public ResponseEntity<ResumenGruposResponse> resumen(@RequestParam("usuario_id") Long usuarioId) {
        return ResponseEntity.ok(grupoService.resumen(usuarioId));
    }

    @GetMapping("/{grupo_id}")
    public ResponseEntity<GrupoDetalleResponse> detalle(@PathVariable("grupo_id") Long grupoId) {
        return ResponseEntity.ok(grupoService.obtenerDetalle(grupoId));
    }

    @PostMapping("/{grupo_id}/integrantes")
    public ResponseEntity<AgregarIntegranteResponse> agregarIntegrante(
            @PathVariable("grupo_id") Long grupoId,
            @Valid @RequestBody AgregarIntegranteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(grupoService.agregarIntegrante(grupoId, request));
    }

    @PostMapping("/{grupo_id}/documentos")
    public ResponseEntity<DocumentoResponse> registrarDocumento(
            @PathVariable("grupo_id") Long grupoId,
            @Valid @RequestBody RegistrarDocumentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(grupoService.registrarDocumento(grupoId, request));
    }
}
