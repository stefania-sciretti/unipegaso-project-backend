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
class PatientControllerIT {

    @Autowired lateinit var mockMvc: MockMvc
    @Autowired lateinit var objectMapper: ObjectMapper

    private fun patientJson(
        firstName: String = "Mario",
        lastName: String = "Rossi",
        fiscalCode: String = "RSSMRA80A01H501U",
        birthDate: String = "1980-01-01",
        email: String = "mario.rossi@example.com",
        phone: String? = null
    ) = mapOf(
        "firstName" to firstName,
        "lastName" to lastName,
        "fiscalCode" to fiscalCode,
        "birthDate" to birthDate,
        "email" to email,
        "phone" to phone
    ).filterValues { it != null }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `GET patients returns 200 with list`() {
        mockMvc.perform(get("/api/patients"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `POST patient creates and returns 201`() {
        mockMvc.perform(
            post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientJson()))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.firstName").value("Mario"))
            .andExpect(jsonPath("$.lastName").value("Rossi"))
            .andExpect(jsonPath("$.fiscalCode").value("RSSMRA80A01H501U"))
            .andExpect(jsonPath("$.id").isNumber)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `POST patient returns 400 on invalid body`() {
        val invalid = mapOf("firstName" to "", "lastName" to "Rossi")
        mockMvc.perform(
            post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `POST patient returns 409 on duplicate fiscal code`() {
        val body = objectMapper.writeValueAsString(patientJson())
        mockMvc.perform(post("/api/patients").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isCreated)
        // second request with same fiscal code
        mockMvc.perform(post("/api/patients").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isConflict)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `GET patient by id returns 200`() {
        // First create a patient
        val result = mockMvc.perform(
            post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientJson()))
        ).andReturn()
        val id = objectMapper.readTree(result.response.contentAsString)["id"].asLong()

        mockMvc.perform(get("/api/patients/$id"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(id))
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `GET patient by id returns 404 when not found`() {
        mockMvc.perform(get("/api/patients/99999"))
            .andExpect(status().isNotFound)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `GET patient search returns results`() {
        mockMvc.perform(
            post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientJson(firstName = "Giulia", lastName = "Verdi", fiscalCode = "VRDGLI90B41F205Z", email = "giulia@test.com")))
        ).andExpect(status().isCreated)

        mockMvc.perform(get("/api/patients?search=Giu"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].firstName").value("Giulia"))
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `GET patient search returns 400 for short query`() {
        mockMvc.perform(get("/api/patients?search=ab"))
            .andExpect(status().isBadRequest)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `PATCH patient updates and returns 200`() {
        val createResult = mockMvc.perform(
            post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientJson()))
        ).andReturn()
        val id = objectMapper.readTree(createResult.response.contentAsString)["id"].asLong()

        val updated = patientJson(firstName = "Marco", phone = "+39 333 1234567")
        mockMvc.perform(
            patch("/api/patients/$id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.firstName").value("Marco"))
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `DELETE patient returns 204`() {
        val createResult = mockMvc.perform(
            post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientJson()))
        ).andReturn()
        val id = objectMapper.readTree(createResult.response.contentAsString)["id"].asLong()

        mockMvc.perform(delete("/api/patients/$id"))
            .andExpect(status().isNoContent)

        mockMvc.perform(get("/api/patients/$id"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `GET patients returns 401 without auth`() {
        mockMvc.perform(get("/api/patients"))
            .andExpect(status().isUnauthorized)
    }
}
