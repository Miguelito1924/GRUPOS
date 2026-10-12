package com.campusstudy.grupos.service;

import com.campusstudy.grupos.client.DependencyUnavailableException;
import com.campusstudy.grupos.client.MateriasClient;
import com.campusstudy.grupos.client.NotificacionesClient;
import com.campusstudy.grupos.client.TareasClient;
import com.campusstudy.grupos.client.UsuariosClient;
import com.campusstudy.grupos.dto.*;
import com.campusstudy.grupos.exception.ApiException;
import com.campusstudy.grupos.model.*;
import com.campusstudy.grupos.repository.DocumentoRepository;
import com.campusstudy.grupos.repository.GrupoRepository;
import com.campusstudy.grupos.repository.IntegranteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GrupoService {

    private final GrupoRepository grupoRepository;
    private final IntegranteRepository integranteRepository;
    private final DocumentoRepository documentoRepository;
    private final MateriasClient materiasClient;
    private final UsuariosClient usuariosClient;
    private final NotificacionesClient notificacionesClient;
    private final TareasClient tareasClient;
    private final GrupoMapper mapper;

    public GrupoService(
            GrupoRepository grupoRepository,
            IntegranteRepository integranteRepository,
            DocumentoRepository documentoRepository,
            MateriasClient materiasClient,
            UsuariosClient usuariosClient,
            NotificacionesClient notificacionesClient,
            TareasClient tareasClient,
            GrupoMapper mapper) {
        this.grupoRepository = grupoRepository;
        this.integranteRepository = integranteRepository;
        this.documentoRepository = documentoRepository;
        this.materiasClient = materiasClient;
        this.usuariosClient = usuariosClient;
        this.notificacionesClient = notificacionesClient;
        this.tareasClient = tareasClient;
        this.mapper = mapper;
    }

    @Transactional
    public GrupoDetalleResponse crearGrupo(CrearGrupoRequest request, Long usuarioId) {
        validarMateria(request.getMateria_id());

        if (grupoRepository.existsByNombreIgnoreCaseAndMateriaId(request.getNombre(), request.getMateria_id())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Bad Request",
                    "El nombre del grupo ya existe en la materia");
        }

        Grupo grupo = new Grupo();
        grupo.setNombre(request.getNombre());
        grupo.setMateriaId(request.getMateria_id());
        grupo.setCreadoPor(usuarioId);
        grupo.setZoomUrl(blankToNull(request.getEnlace_zoom()));
        grupo.setJamUrl(blankToNull(request.getEnlace_jam()));

        Integrante lider = new Integrante();
        lider.setGrupo(grupo);
        lider.setUsuarioId(usuarioId);
        lider.setRol(RolIntegrante.lider);
        grupo.getIntegrantes().add(lider);

        Grupo guardado = grupoRepository.save(grupo);

        notificar("grupo_creado", Map.of(
                "grupo_id", guardado.getId(),
                "nombre", guardado.getNombre(),
                "materia_id", guardado.getMateriaId(),
                "usuario_id", usuarioId
        ));

        return mapper.toCreacion(guardado);
    }

    @Transactional(readOnly = true)
    public List<GrupoListadoResponse> listarMisGrupos(Long usuarioId) {
        return grupoRepository.findByIntegranteUsuarioId(usuarioId).stream()
                .map(mapper::toListado)
                .toList();
    }

    @Transactional(readOnly = true)
    public GrupoDetalleResponse obtenerDetalle(Long grupoId) {
        Grupo grupo = grupoRepository.findByIdWithDetails(grupoId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Not Found",
                        "El grupo no existe"));

        grupo.setDocumentos(documentoRepository.findByGrupoIdOrderByFechaActualizacionDesc(grupoId));

        String nombreMateria = materiasClient.obtenerNombreMateria(grupo.getMateriaId())
                .orElse("Materia " + grupo.getMateriaId());

        List<EventoCalendarioResponse> eventos = tareasClient.obtenerEventosGrupo(
                grupo.getId(), grupo.getNombre(), nombreMateria);

        return mapper.toDetalle(grupo, nombreMateria, eventos);
    }

    @Transactional(readOnly = true)
    public ResumenGruposResponse resumen(Long usuarioId) {
        return new ResumenGruposResponse(
                usuarioId,
                grupoRepository.countByIntegranteUsuarioId(usuarioId),
                grupoRepository.countActivosByIntegranteUsuarioId(usuarioId),
                grupoRepository.countLideradosByUsuarioId(usuarioId),
                grupoRepository.countMiembroByUsuarioId(usuarioId)
        );
    }

    @Transactional
    public AgregarIntegranteResponse agregarIntegrante(Long grupoId, AgregarIntegranteRequest request) {
        Grupo grupo = grupoRepository.findByIdWithIntegrantes(grupoId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Not Found",
                        "El grupo no existe"));

        validarUsuario(request.getUsuario_id());

        if (integranteRepository.existsByGrupoIdAndUsuarioId(grupoId, request.getUsuario_id())) {
            throw new ApiException(HttpStatus.CONFLICT, "Conflict",
                    "El usuario ya es integrante del grupo");
        }

        Integrante integrante = new Integrante();
        integrante.setGrupo(grupo);
        integrante.setUsuarioId(request.getUsuario_id());
        integrante.setRol(RolIntegrante.miembro);
        grupo.getIntegrantes().add(integrante);
        integranteRepository.save(integrante);

        notificar("integrante_agregado", Map.of(
                "grupo_id", grupoId,
                "usuario_id", request.getUsuario_id(),
                "rol", RolIntegrante.miembro.name()
        ));

        return new AgregarIntegranteResponse(
                "Integrante agregado correctamente",
                mapper.toIntegrante(integrante)
        );
    }

    @Transactional
    public DocumentoResponse registrarDocumento(Long grupoId, RegistrarDocumentoRequest request) {
        Grupo grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Not Found",
                        "El grupo no existe"));

        if (request.getUrl() == null || !(request.getUrl().startsWith("http://") || request.getUrl().startsWith("https://"))) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Bad Request",
                    "La url del documento debe ser válida");
        }

        Documento documento = new Documento();
        documento.setGrupo(grupo);
        documento.setNombre(request.getNombre());
        documento.setTipo(TipoDocumento.valueOf(request.getTipo()));
        documento.setUrl(request.getUrl());

        Documento guardado = documentoRepository.save(documento);
        return mapper.toDocumento(guardado);
    }

    private void validarMateria(Long materiaId) {
        try {
            Optional<String> materia = materiasClient.obtenerNombreMateria(materiaId);
            if (materia.isEmpty()) {
                throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, "Unprocessable Entity",
                        "materia_id no existe en materias-service");
            }
        } catch (DependencyUnavailableException ex) {
            throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE, "Service Unavailable",
                    "Servicio de materias no disponible");
        }
    }

    private void validarUsuario(Long usuarioId) {
        try {
            if (!usuariosClient.existeUsuario(usuarioId)) {
                throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, "Unprocessable Entity",
                        "usuario_id no existe en usuarios-service");
            }
        } catch (DependencyUnavailableException ex) {
            throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE, "Service Unavailable",
                    "Servicio de usuarios no disponible");
        }
    }

    private void notificar(String tipo, Map<String, Object> data) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("tipo", tipo);
        payload.put("origen", "grupos-service");
        payload.put("data", data);
        notificacionesClient.enviarNotificacion(payload);
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }
}
