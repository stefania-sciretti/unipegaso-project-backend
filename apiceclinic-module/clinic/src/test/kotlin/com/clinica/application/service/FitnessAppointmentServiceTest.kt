package com.clinica.application.service

import com.clinica.application.domain.AppointmentStatus
import com.clinica.application.domain.FitnessAppointment
import com.clinica.application.domain.Patient
import com.clinica.application.domain.Specialist
import com.clinica.doors.outbound.database.dao.FitnessAppointmentDao
import com.clinica.doors.outbound.database.dao.PatientDao
import com.clinica.doors.outbound.database.dao.SpecialistDao
import com.clinica.dto.FitnessAppointmentRequest
import com.clinica.dto.FitnessAppointmentStatusRequest
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class FitnessAppointmentServiceTest {

    @MockK private lateinit var appointmentDao: FitnessAppointmentDao
    @MockK private lateinit var patientDao: PatientDao
    @MockK private lateinit var specialistDao: SpecialistDao

    @InjectMockKs
    private lateinit var service: FitnessAppointmentService

    private val fixedTime = LocalDateTime.of(2025, 6, 15, 10, 0)

    private fun buildPatient(id: Long = 1L) = Patient(
        id = id, firstName = "Mario", lastName = "Rossi",
        fiscalCode = "RSSMRA80A01H501U", birthDate = LocalDate.of(1980, 1, 1),
        email = "mario@example.com"
    )

    private fun buildSpecialist(id: Long = 10L) = Specialist(
        id = id, firstName = "Anna", lastName = "Verdi",
        role = "TRAINER", email = "anna@example.com"
    )

    private fun buildFitnessAppointment(
        id: Long = 1L,
        status: AppointmentStatus = AppointmentStatus.BOOKED
    ) = FitnessAppointment(
        id = id, patient = buildPatient(), specialist = buildSpecialist(),
        scheduledAt = fixedTime, serviceType = "Personal Training",
        status = status, notes = "Note", updatedAt = fixedTime
    )

    private fun buildRequest() = FitnessAppointmentRequest(
        patientId = 1L, specialistId = 10L, scheduledAt = fixedTime,
        serviceType = "Personal Training", notes = "Note"
    )

    // findAll

    @Test
    fun `findAll returns mapped responses`() {
        every { appointmentDao.findAll(null, null, null) } returns
            listOf(buildFitnessAppointment(1L), buildFitnessAppointment(2L))

        assertEquals(2, service.findAll(null, null, null).size)
    }

    // findById

    @Test
    fun `findById returns response when found`() {
        every { appointmentDao.findById(1L) } returns buildFitnessAppointment()

        val result = service.findById(1L)

        assertEquals(1L, result.id)
        assertEquals("BOOKED", result.status)
        assertEquals("Personal Training", result.serviceType)
        assertEquals("Mario Rossi", result.patientFullName)
    }

    @Test
    fun `findById throws NoSuchElementException when not found`() {
        every { appointmentDao.findById(99L) } returns null
        assertThrows<NoSuchElementException> { service.findById(99L) }
    }

    // create

    @Test
    fun `create saves appointment with BOOKED status`() {
        val saved = buildFitnessAppointment(id = 5L)
        every { patientDao.findById(1L) } returns buildPatient()
        every { specialistDao.findById(10L) } returns buildSpecialist()
        every { appointmentDao.save(any()) } returns saved

        val result = service.create(buildRequest())

        assertEquals(5L, result.id)
        assertEquals("BOOKED", result.status)
    }

    @Test
    fun `create throws NoSuchElementException when client not found`() {
        every { patientDao.findById(1L) } returns null

        assertThrows<NoSuchElementException> { service.create(buildRequest()) }
        verify(exactly = 0) { appointmentDao.save(any()) }
    }

    @Test
    fun `create throws NoSuchElementException when staff not found`() {
        every { patientDao.findById(1L) } returns buildPatient()
        every { specialistDao.findById(10L) } returns null

        assertThrows<NoSuchElementException> { service.create(buildRequest()) }
        verify(exactly = 0) { appointmentDao.save(any()) }
    }

    // updateStatus

    @Test
    fun `updateStatus changes appointment status`() {
        val appointment = buildFitnessAppointment(status = AppointmentStatus.BOOKED)
        val updated = appointment.copy(status = AppointmentStatus.CONFIRMED)
        every { appointmentDao.findById(1L) } returns appointment
        every { appointmentDao.save(any()) } returns updated

        val result = service.updateStatus(1L, FitnessAppointmentStatusRequest("CONFIRMED"))

        assertEquals("CONFIRMED", result.status)
    }

    @Test
    fun `updateStatus throws NoSuchElementException when not found`() {
        every { appointmentDao.findById(99L) } returns null

        assertThrows<NoSuchElementException> {
            service.updateStatus(99L, FitnessAppointmentStatusRequest("CONFIRMED"))
        }
    }

    @Test
    fun `updateStatus throws IllegalArgumentException for invalid status`() {
        every { appointmentDao.findById(1L) } returns buildFitnessAppointment()

        assertThrows<IllegalArgumentException> {
            service.updateStatus(1L, FitnessAppointmentStatusRequest("INVALID"))
        }
    }

    // delete (soft cancel)

    @Test
    fun `delete cancels appointment instead of physically deleting`() {
        val appointment = buildFitnessAppointment(status = AppointmentStatus.BOOKED)
        val cancelled = appointment.copy(status = AppointmentStatus.CANCELLED)
        every { appointmentDao.findById(1L) } returns appointment
        every { appointmentDao.save(any()) } returns cancelled

        service.delete(1L)

        verify { appointmentDao.save(any()) }
    }

    @Test
    fun `delete throws NoSuchElementException when not found`() {
        every { appointmentDao.findById(99L) } returns null

        assertThrows<NoSuchElementException> { service.delete(99L) }
        verify(exactly = 0) { appointmentDao.save(any()) }
    }
}
