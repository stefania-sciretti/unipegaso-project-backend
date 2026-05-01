package com.clinica.integration

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SpecialistControllerIT {

    @Autowired lateinit var mockMvc: MockMvc
    @Autowired lateinit var objectMapper: ObjectMapper

    private fun specialistJson(
        firstName: String = "Anna",
        lastName: String = "Bianchi",
        role: String = "NUTRITIONIST",
        bio: String? = "Expert nutritionist",
        email: String = "anna.bianchi@apiceclinic.it"
    ) = mapOf(
        "firstName" to firstName,
        "lastName" to lastName,
        "role" to role,
        "bio" to bio,
        "email" to email
    ).filterValues { it != null }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `GET specialists returns 200`() {
        mockMvc.perform(get("/api/specialists"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `POST specialist creates and returns 201`() {
        mockMvc.perform(
            post("/api/specialists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(specialistJson()))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.firstName").value("Anna"))
            .andExpect(jsonPath("$.role").value("NUTRITIONIST"))
            .andExpect(jsonPath("$.id").isNumber)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `POST specialist returns 400 on invalid body`() {
        val invalid = mapOf("firstName" to "Anna")
        mockMvc.perform(
            post("/api/specialists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `POST specialist returns 409 on duplicate email`() {
        val body = objectMapper.writeValueAsString(specialistJson())
        mockMvc.perform(post("/api/specialists").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isCreated)
        mockMvc.perform(post("/api/specialists").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isConflict)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `GET specialist by id returns 200`() {
        val result = mockMvc.perform(
            post("/api/specialists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(specialistJson()))
        ).andReturn()
        val id = objectMapper.readTree(result.response.contentAsString)["id"].asLong()

        mockMvc.perform(get("/api/specialists/$id"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(id))
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `GET specialist by id returns 404 when not found`() {
        mockMvc.perform(get("/api/specialists/99999"))
            .andExpect(status().isNotFound)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `GET specialists filtered by role`() {
        mockMvc.perform(
            post("/api/specialists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(specialistJson(role = "TRAINER", email = "trainer@test.it")))
        ).andExpect(status().isCreated)

        mockMvc.perform(get("/api/specialists?role=TRAINER"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].role").value("TRAINER"))
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `PUT specialist updates and returns 200`() {
        val createResult = mockMvc.perform(
            post("/api/specialists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(specialistJson()))
        ).andReturn()
        val id = objectMapper.readTree(createResult.response.contentAsString)["id"].asLong()

        val updated = specialistJson(firstName = "Anna", lastName = "Verdi", role = "PHYSIOTHERAPIST", email = "anna.bianchi@apiceclinic.it")
        mockMvc.perform(
            put("/api/specialists/$id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.role").value("PHYSIOTHERAPIST"))
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `DELETE specialist returns 204`() {
        val createResult = mockMvc.perform(
            post("/api/specialists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(specialistJson()))
        ).andReturn()
        val id = objectMapper.readTree(createResult.response.contentAsString)["id"].asLong()

        mockMvc.perform(delete("/api/specialists/$id"))
            .andExpect(status().isNoContent)

        mockMvc.perform(get("/api/specialists/$id"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `GET specialists returns 401 without auth`() {
        mockMvc.perform(get("/api/specialists"))
            .andExpect(status().isUnauthorized)
    }
}
