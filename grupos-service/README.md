# grupos-service

Microservicio de **CampusStudy** encargado de administrar grupos de trabajo por materia, integrantes, herramientas colaborativas (Zoom/Jam), documentos y resumen para estadísticas.

- **API Base:** `/api/v1/`
- **Puerto:** `8082`
- **Stack:** Java 17, Spring Boot 3, JPA, H2

## Integrantes

| Nombre | ID |
|--------|-----|
| Miguel Angel Beltran Rache | 1048977 |
| Alma Carina Hernandez Fernandez | 964358 |
| Samuel Orlando Caro Brahoma | — |

## Requisitos

- JDK 17+
- Maven 3.9+

## Instalar y ejecutar

```bash
cd grupos-service
mvn clean spring-boot:run
```

La API queda en: `http://localhost:8082`

Consola H2: `http://localhost:8082/h2-console`  
- JDBC URL: `jdbc:h2:mem:gruposdb`  
- User: `sa` / password vacío

## Configuración

Archivo: `src/main/resources/application.yml`

| Propiedad | Descripción | Default |
|-----------|-------------|---------|
| `server.port` | Puerto del servicio | `8082` |
| `campusstudy.auth.demo-mode` | Auth demo: `Bearer <usuario_id>` | `true` |
| `campusstudy.stubs.enabled` | Simula materias/usuarios/tareas/notif | `true` |
| `campusstudy.clients.timeout-ms` | Timeout HTTP a dependencias | `3000` |
| `campusstudy.clients.max-retries` | Reintentos ante fallo de red | `2` |
| `campusstudy.clients.*-base-url` | URLs de otros microservicios | ver yml |

Para apuntar a servicios reales:

```yaml
campusstudy:
  stubs:
    enabled: false
  clients:
    materias-base-url: http://localhost:8083
    usuarios-base-url: http://localhost:8081
    notificaciones-base-url: http://localhost:8084
    tareas-base-url: http://localhost:8085
```

### Auth en modo demo

```
Authorization: Bearer 692464
```

El número es el `usuario_id` autenticado.

### Stubs de prueba

| ID | Efecto |
|----|--------|
| `materia_id=999` / `usuario_id=999` | No existe → **422** |
| `materia_id=998` / `usuario_id=998` | Dependencia caída → **503** |

## Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/api/v1/grupos` | Crear grupo |
| `GET` | `/api/v1/grupos` | Listar grupos del usuario |
| `GET` | `/api/v1/grupos/{grupo_id}` | Detalle + docs + calendario |
| `GET` | `/api/v1/grupos/resumen?usuario_id=` | Resumen para estadísticas |
| `POST` | `/api/v1/grupos/{grupo_id}/integrantes` | Agregar integrante |
| `POST` | `/api/v1/grupos/{grupo_id}/documentos` | Registrar documento |

### Ejemplos rápidos

```bash
# Crear grupo
curl -X POST http://localhost:8082/api/v1/grupos ^
  -H "Authorization: Bearer 692464" ^
  -H "Content-Type: application/json" ^
  -d "{\"nombre\":\"Grupo 1 - Sistemas Distribuidos\",\"materia_id\":12,\"enlace_zoom\":\"https://zoom.us/j/123456789\"}"

# Listar mis grupos
curl http://localhost:8082/api/v1/grupos -H "Authorization: Bearer 692464"

# Detalle
curl http://localhost:8082/api/v1/grupos/1 -H "Authorization: Bearer 692464"

# Resumen
curl "http://localhost:8082/api/v1/grupos/resumen?usuario_id=692464" -H "Authorization: Bearer 692464"

# Agregar integrante
curl -X POST http://localhost:8082/api/v1/grupos/1/integrantes ^
  -H "Authorization: Bearer 692464" ^
  -H "Content-Type: application/json" ^
  -d "{\"usuario_id\":692465}"

# Documento
curl -X POST http://localhost:8082/api/v1/grupos/1/documentos ^
  -H "Authorization: Bearer 692464" ^
  -H "Content-Type: application/json" ^
  -d "{\"nombre\":\"Avance.docx\",\"tipo\":\"word\",\"url\":\"https://docs.google.com/document/d/xyz\"}"
```

## Dependencias con otros servicios

| Servicio | Relación |
|----------|----------|
| **materias-service** | Valida `materia_id` al crear grupo |
| **usuarios-service** | Valida `usuario_id` al invitar |
| **notificaciones-service** | Evento al crear grupo / agregar integrante (no bloquea si falla) |
| **tareas-service** | Eventos de calendario en el detalle del grupo |
| **Frontend / tareas / estadísticas** | Consumen esta API |

## Probar

```bash
mvn test
```

Cubre creación, duplicados, 401, listado, 422, 503, documentos y resumen.

## Estructura

```
grupos-service/
├── controller/
├── service/
├── repository/
├── model/
├── dto/
├── client/
├── config/
├── exception/
└── tests/
```

## Documentación de entrega

Ver `docs/ENTREGA_2_CampusStudy_Grupos.md`
