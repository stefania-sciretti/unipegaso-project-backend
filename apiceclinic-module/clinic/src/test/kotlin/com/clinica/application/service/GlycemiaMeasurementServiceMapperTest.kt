package com.clinica.application.service

import com.clinic.model.GlycemiaMeasurementResponse
import com.clinica.application.domain.*
import com.clinica.application.mappers.toResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

class GlycemiaMeasurementServiceMapperTest {

    private val fixedTime = LocalDateTime.of(2026, 4, 20, 8, 0)

    private val patient = Patient(
        id = 3L, firstName = "Giulia", lastName = "Ferretti",
        fiscalCode = "FRTGLI90A41H501Z", birthDate = LocalDate.of(1990, 1, 1),
        email = "giulia@example.com"
    )

    private val specialist = Specialist(
        id = 1L, firstName = "Simona", lastName = "Ruberti",
        role = "NUTRITIONIST", email = "simona@example.com"
    )

    @Test
    fun `toResponse maps NORMALE classification for A_DIGIUNO below 100`() {
        val measurement = GlycemiaMeasurement(
            id = 1L, patient = patient, specialist = specialist,
            measuredAt = fixedTime, valueMgDl = 92,
            context = GlycemiaContext.A_DIGIUNO, notes = null,
            createdAt = fixedTime, updatedAt = fixedTime
        )

        val response = measurement.toResponse()

        assertEquals(1L, response.id)
        assertEquals(3L, response.patientId)
        assertEquals("Giulia", response.patientFirstName)
        assertEquals("Ferretti", response.patientLastName)
        assertEquals(1L, response.specialistId)
        assertEquals("Simona", response.specialistFirstName)
        assertEquals("Ruberti", response.specialistLastName)
        assertEquals(fixedTime.atOffset(ZoneOffset.UTC), response.measuredAt)
        assertEquals(92, response.valueMgDl)
        assertEquals(GlycemiaMeasurementResponse.Context.A_DIGIUNO, response.context)
        assertEquals(GlycemiaMeasurementResponse.Classification.NORMALE, response.classification)
        assertNull(response.notes)
        assertEquals(fixedTime.atOffset(ZoneOffset.UTC), response.createdAt)
        assertEquals(fixedTime.atOffset(ZoneOffset.UTC), response.updatedAt)
    }

    @Test
    fun `toResponse maps ATTENZIONE classification for A_DIGIUNO between 100 and 125`() {
        val measurement = GlycemiaMeasurement(
            id = 2L, patient = patient, specialist = specialist,
            measuredAt = fixedTime, valueMgDl = 112,
            context = GlycemiaContext.A_DIGIUNO, notes = "Borderline",
            createdAt = fixedTime, updatedAt = fixedTime
        )

        val response = measurement.toResponse()

        assertEquals(GlycemiaMeasurementResponse.Classification.ATTENZIONE, response.classification)
        assertEquals("Borderline", response.notes)
    }

    @Test
    fun `toResponse maps ELEVATA classification for A_DIGIUNO at 126 or above`() {
        val measurement = GlycemiaMeasurement(
            id = 3L, patient = patient, specialist = specialist,
            measuredAt = fixedTime, valueMgDl = 130,
            context = GlycemiaContext.A_DIGIUNO, notes = null,
            createdAt = fixedTime, updatedAt = fixedTime
        )

        val response = measurement.toResponse()

        assertEquals(GlycemiaMeasurementResponse.Classification.ELEVATA, response.classification)
    }

    @Test
    fun `toResponse maps POST_PASTO_2H context correctly`() {
        val measurement = GlycemiaMeasurement(
            id = 4L, patient = patient, specialist = specialist,
            measuredAt = fixedTime, valueMgDl = 135,
            context = GlycemiaContext.POST_PASTO_2H, notes = null,
            createdAt = fixedTime, updatedAt = fixedTime
        )

        val response = measurement.toResponse()

        assertEquals(GlycemiaMeasurementResponse.Context.POST_PASTO_2H, response.context)
        assertEquals(GlycemiaMeasurementResponse.Classification.NORMALE, response.classification)
    }
}
