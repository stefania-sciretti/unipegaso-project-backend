package com.clinica.application.service

import com.clinica.application.domain.Appointment
import com.clinica.application.domain.AppointmentStatusEnum
import com.clinica.application.domain.Patient
import com.clinica.application.domain.Specialist
import com.clinica.application.mappers.toResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

class AppointmentServiceMapperTest {

    private val fixedTime = LocalDateTime.of(2025, 6, 15, 10, 0)

    private val patient = Patient(
        id = 1L, firstName = "Mario", lastName = "Rossi",
        fiscalCode = "RSSMRA80A01H501U", birthDate = LocalDate.of(1980, 1, 1),
        email = "mario@example.com"
    )

    private val specialist = Specialist(
        id = 2L, firstName = "Luigi", lastName = "Bianchi",
        role = "PERSONAL_TRAINER", email = "luigi@example.com",
        areaId = 2L, areaName = "Sport"
    )

    private val appointment = Appointment(
        id = 10L,
        patient = patient,
        specialist = specialist,
        scheduledAt = fixedTime,
        serviceType = "PERSONAL_TRAINING",
        status = AppointmentStatusEnum.CONFIRMED,
        notes = "Test note",
        price = BigDecimal("75.00"),
        createdAt = fixedTime,
        areaId = 2L,
        areaName = "Sport",
        updatedAt = fixedTime
    )

    @Test
    fun `toResponse maps all fields correctly`() {
        val response = appointment.toResponse()

        assertEquals(10L, response.id)
        assertEquals(1L, response.patientId)
        assertEquals("Rossi Mario", response.patientFullName)
        assertEquals(2L, response.specialistId)
        assertEquals("Luigi Bianchi", response.specialistFullName)
        assertEquals("PERSONAL_TRAINER", response.specialistRole)
        assertEquals(fixedTime.atOffset(ZoneOffset.UTC), response.scheduledAt)
        assertEquals("PERSONAL_TRAINING", response.serviceType)
        assertEquals("CONFIRMED", response.status)
        assertEquals("Test note", response.notes)
        assertFalse(response.hasReport)
        assertEquals(2L, response.areaId)
        assertEquals("Sport", response.areaName)
        assertEquals(75.0, response.price)
        assertEquals(fixedTime.atOffset(ZoneOffset.UTC), response.createdAt)
    }

    @Test
    fun `toResponse maps null notes correctly`() {
        val response = appointment.copy(notes = null).toResponse()
        assertNull(response.notes)
    }

    @Test
    fun `toResponse maps null area correctly`() {
        val response = appointment.copy(areaId = null, areaName = null).toResponse()
        assertNull(response.areaId)
        assertNull(response.areaName)
    }
}
