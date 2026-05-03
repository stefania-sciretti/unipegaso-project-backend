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
class ServiceControllerIT {

    @Autowired lateinit var mockMvc: MockMvc
    @Autowired lateinit var objectMapper: ObjectMapper

    /**
     * Creates a specialist and returns its assigned ID.
     * Needed because services require a valid specialist FK.
     */
    private fun createSpecialistAndGetId(email: String = "svc.specialist@apiceclinic.it"): Long {
        val body = objectMapper.writeValueAsString(
            mapOf(
                "firstName" to "Test",
                "lastName" to "Specialist",
                "role" to "NUTRITIONIST",
                "email" to email
            )
        )
        val result = mockMvc.perform(
            post("/api/specialists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isCreated).andReturn()
        return objectMapper.readTree(result.response.contentAsString)["id"].asLong()
    }

    private fun serviceJson(
        service: String = "Nutritional consultation",
        price: Double = 80.0,
        specialistId: Long
    ) = mapOf(
        "service" to service,
        "price" to price,
        "specialistId" to specialistId
    )

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `GET services returns 200 with list`() {
        mockMvc.perform(get("/api/services"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `POST service creates and returns 201`() {
        val specialistId = createSpecialistAndGetId()

        mockMvc.perform(
            post("/api/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(serviceJson(specialistId = specialistId)))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.service").value("Nutritional consultation"))
            .andExpect(jsonPath("$.price").value(80.0))
            .andExpect(jsonPath("$.specialistId").value(specialistId))
            .andExpect(jsonPath("$.id").isNumber)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `POST service returns 400 on invalid body`() {
        // Missing required "service" field (non-nullable String): Jackson cannot deserialize → 400
        val invalid = mapOf("price" to 80.0, "specialistId" to 1)
        mockMvc.perform(
            post("/api/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `POST service returns 404 when specialist not found`() {
        val body = objectMapper.writeValueAsString(serviceJson(specialistId = 99999L))
        mockMvc.perform(
            post("/api/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        )
            .andExpect(status().isNotFound)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `GET service by id returns 200`() {
        val specialistId = createSpecialistAndGetId(email = "svc2@apiceclinic.it")
        val createResult = mockMvc.perform(
            post("/api/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(serviceJson(specialistId = specialistId)))
        ).andReturn()
        val id = objectMapper.readTree(createResult.response.contentAsString)["id"].asLong()

        mockMvc.perform(get("/api/services/$id"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(id))
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `GET service by id returns 404 when not found`() {
        mockMvc.perform(get("/api/services/99999"))
            .andExpect(status().isNotFound)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `GET services filtered by specialistId`() {
        val specialistId = createSpecialistAndGetId(email = "svc3@apiceclinic.it")
        mockMvc.perform(
            post("/api/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(serviceJson(service = "Diet plan", specialistId = specialistId)))
        ).andExpect(status().isCreated)

        mockMvc.perform(get("/api/services?specialistId=$specialistId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].specialistId").value(specialistId))
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `PUT service updates and returns 200`() {
        val specialistId = createSpecialistAndGetId(email = "svc4@apiceclinic.it")
        val createResult = mockMvc.perform(
            post("/api/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(serviceJson(specialistId = specialistId)))
        ).andReturn()
        val id = objectMapper.readTree(createResult.response.contentAsString)["id"].asLong()

        val updated = serviceJson(service = "Updated consultation", price = 100.0, specialistId = specialistId)
        mockMvc.perform(
            put("/api/services/$id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.service").value("Updated consultation"))
            .andExpect(jsonPath("$.price").value(100.0))
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `PUT service returns 404 when service not found`() {
        val specialistId = createSpecialistAndGetId(email = "svc5@apiceclinic.it")
        val body = objectMapper.writeValueAsString(serviceJson(specialistId = specialistId))
        mockMvc.perform(
            put("/api/services/99999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        )
            .andExpect(status().isNotFound)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `DELETE service returns 204`() {
        val specialistId = createSpecialistAndGetId(email = "svc6@apiceclinic.it")
        val createResult = mockMvc.perform(
            post("/api/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(serviceJson(specialistId = specialistId)))
        ).andReturn()
        val id = objectMapper.readTree(createResult.response.contentAsString)["id"].asLong()

        mockMvc.perform(delete("/api/services/$id"))
            .andExpect(status().isNoContent)

        mockMvc.perform(get("/api/services/$id"))
            .andExpect(status().isNotFound)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `DELETE service returns 404 when not found`() {
        mockMvc.perform(delete("/api/services/99999"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `GET services returns 401 without auth`() {
        mockMvc.perform(get("/api/services"))
            .andExpect(status().isUnauthorized)
    }
}
