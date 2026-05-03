package com.clinica.application.service

import com.clinica.application.domain.Appointment
import com.clinica.application.domain.AppointmentStatusEnum
import com.clinica.application.domain.Patient
import com.clinica.application.domain.Specialist
import com.clinica.application.mappers.toResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
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
        role = "Nutrizionista", email = "luigi@example.com"
    )

    private val appointment = Appointment(
        id = 10L,
        patient = patient,
        specialist = specialist,
        scheduledAt = fixedTime,
        visitType = "Routine",
        status = AppointmentStatusEnum.CONFIRMED,
        notes = "Test note",
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
        assertEquals("Nutrizionista", response.specialistSpecialization)
        assertEquals(fixedTime.atOffset(ZoneOffset.UTC), response.scheduledAt)
        assertEquals("Routine", response.visitType)
        assertEquals("CONFIRMED", response.status)
        assertEquals("Test note", response.notes)
        assertFalse(response.hasReport)
        assertEquals(fixedTime.atOffset(ZoneOffset.UTC), response.createdAt)
    }

    @Test
    fun `toResponse maps null notes correctly`() {
        val response = appointment.copy(notes = null).toResponse()
        assertNull(response.notes)
    }
}
