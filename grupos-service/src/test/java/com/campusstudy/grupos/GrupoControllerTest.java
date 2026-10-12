package com.campusstudy.grupos;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GrupoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crearGrupoValidoRetorna201ConLider() throws Exception {
        mockMvc.perform(post("/api/v1/grupos")
                        .header("Authorization", "Bearer 692464")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Grupo 1 - Sistemas Distribuidos",
                                  "materia_id": 12,
                                  "enlace_zoom": "https://zoom.us/j/123456789",
                                  "enlace_jam": "https://jamboard.google.com/d/abc12345"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.grupo_id").isNumber())
                .andExpect(jsonPath("$.integrantes[0].rol").value("lider"))
                .andExpect(jsonPath("$.integrantes[0].usuario_id").value(692464));
    }

    @Test
    void duplicarNombreEnMismaMateriaRetorna400() throws Exception {
        String body = """
                {
                  "nombre": "Grupo Dup Test",
                  "materia_id": 12
                }
                """;

        mockMvc.perform(post("/api/v1/grupos")
                        .header("Authorization", "Bearer 692464")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/grupos")
                        .header("Authorization", "Bearer 692464")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listarSinTokenRetorna401() throws Exception {
        mockMvc.perform(get("/api/v1/grupos"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void listarConTokenRetorna200() throws Exception {
        mockMvc.perform(post("/api/v1/grupos")
                        .header("Authorization", "Bearer 692464")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Grupo Listado","materia_id":15}
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/grupos")
                        .header("Authorization", "Bearer 692464"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].grupo_id").exists());
    }

    @Test
    void materiaInexistenteRetorna422() throws Exception {
        mockMvc.perform(post("/api/v1/grupos")
                        .header("Authorization", "Bearer 692464")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Grupo X","materia_id":999}
                                """))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void materiasCaidoRetorna503() throws Exception {
        mockMvc.perform(post("/api/v1/grupos")
                        .header("Authorization", "Bearer 692464")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Grupo Y","materia_id":998}
                                """))
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    void detalleOrdenaDocumentosYResumen() throws Exception {
        MvcResult created = mockMvc.perform(post("/api/v1/grupos")
                        .header("Authorization", "Bearer 692464")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Grupo Docs","materia_id":12}
                                """))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode root = objectMapper.readTree(created.getResponse().getContentAsString());
        long grupoId = root.get("grupo_id").asLong();

        mockMvc.perform(post("/api/v1/grupos/" + grupoId + "/documentos")
                        .header("Authorization", "Bearer 692464")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre":"A.docx",
                                  "tipo":"word",
                                  "url":"https://docs.google.com/document/d/a"
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/grupos/" + grupoId + "/documentos")
                        .header("Authorization", "Bearer 692464")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre":"B.xlsx",
                                  "tipo":"excel",
                                  "url":"https://docs.google.com/spreadsheets/d/b"
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/grupos/" + grupoId)
                        .header("Authorization", "Bearer 692464"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.documentos").isArray())
                .andExpect(jsonPath("$.calendario_eventos").isArray())
                .andExpect(jsonPath("$.nombre_materia").value("Sistemas Distribuidos"));

        MvcResult resumen = mockMvc.perform(get("/api/v1/grupos/resumen")
                        .param("usuario_id", "692464")
                        .header("Authorization", "Bearer 692464"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuario_id").value(692464))
                .andReturn();

        JsonNode resumenJson = objectMapper.readTree(resumen.getResponse().getContentAsString());
        assertThat(resumenJson.get("total_grupos").asLong()).isGreaterThanOrEqualTo(1);
    }

    @Test
    void usuarioInexistenteAlInvitarRetorna422() throws Exception {
        MvcResult created = mockMvc.perform(post("/api/v1/grupos")
                        .header("Authorization", "Bearer 692464")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Grupo Invite","materia_id":12}
                                """))
                .andExpect(status().isCreated())
                .andReturn();

        long grupoId = objectMapper.readTree(created.getResponse().getContentAsString())
                .get("grupo_id").asLong();

        mockMvc.perform(post("/api/v1/grupos/" + grupoId + "/integrantes")
                        .header("Authorization", "Bearer 692464")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"usuario_id":999}
                                """))
                .andExpect(status().isUnprocessableEntity());
    }
}
