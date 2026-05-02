package com.clinica.application.service

import com.clinica.application.domain.Appointment
import com.clinica.application.domain.AppointmentStatusEnum
import com.clinica.application.domain.Patient
import com.clinica.application.domain.Specialist
import com.clinica.doors.outbound.database.dao.AppointmentDao
import com.clinica.doors.outbound.database.dao.PatientDao
import com.clinica.doors.outbound.database.dao.SpecialistDao
import com.clinica.dto.AppointmentRequest
import com.clinica.dto.AppointmentStatusRequest
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class AppointmentServiceTest {

    @MockK private lateinit var appointmentDao: AppointmentDao
    @MockK private lateinit var patientDao: PatientDao
    @MockK private lateinit var specialistDao: SpecialistDao

    @InjectMockKs
    private lateinit var service: AppointmentService

    private val fixedTime = LocalDateTime.of(2025, 6, 15, 10, 0)

    private fun buildPatient(id: Long = 1L) = Patient(
        id = id, firstName = "Mario",
        lastName = "Rossi",
        fiscalCode = "RSSMRA80A01H501U",
        birthDate = LocalDate.of(1980, 1, 1),
        email = "mario@example.com"
    )

    private fun buildSpecialist(id: Long = 1L) = Specialist(
        id = id,
        firstName = "Luigi",
        lastName = "Bianchi",
        role = "Nutrizionista",
        email = "luigi@example.com",
    )

    private fun buildAppointment(
        id: Long = 1L,
        status: AppointmentStatusEnum = AppointmentStatusEnum.BOOKED
    ) = Appointment(
        id = id, patient = buildPatient(), specialist = buildSpecialist(),
        scheduledAt = fixedTime, visitType = "Routine", status = status,
        notes = "Note", updatedAt = fixedTime
    )

    private fun buildRequest() = AppointmentRequest(
        patientId = 1L, specialistId = 1L, scheduledAt = fixedTime,
        visitType = "Routine", notes = "Note"
    )

    // findAll

    @Test
    fun `findAll returns mapped responses`() {
        every { appointmentDao.findAll(null, null, null) } returns
            listOf(buildAppointment(1L), buildAppointment(2L))

        val result = service.findAll(null, null, null)

        assertEquals(2, result.size)
        assertEquals(1L, result[0].patientId)
    }

    @Test
    fun `findAll filters by patientId`() {
        every { appointmentDao.findAll(1L, null, null) } returns listOf(buildAppointment())

        val result = service.findAll(1L, null, null)

        assertEquals(1, result.size)
        verify { appointmentDao.findAll(1L, null, null) }
    }

    // findById

    @Test
    fun `findById returns appointment response when found`() {
        every { appointmentDao.findById(1L) } returns buildAppointment()

        val result = service.findById(1L)

        assertEquals(1L, result.id)
        assertEquals("BOOKED", result.status)
        assertEquals("Mario Rossi", result.patientFullName)
        assertEquals("Luigi Bianchi", result.specialistFullName)
    }

    @Test
    fun `findById throws NoSuchElementException when not found`() {
        every { appointmentDao.findById(99L) } returns null

        val ex = assertThrows<NoSuchElementException> { service.findById(99L) }
        assert(ex.message!!.contains("99"))
    }

    // create

    @Test
    fun `create saves appointment with BOOKED status`() {
        val saved = buildAppointment(id = 5L)
        every { patientDao.findById(1L) } returns buildPatient()
        every { specialistDao.findById(1L) } returns buildSpecialist()
        every { appointmentDao.save(any()) } returns saved

        val result = service.create(buildRequest())

        assertEquals(5L, result.id)
        assertEquals("BOOKED", result.status)
        verify { appointmentDao.save(any()) }
    }

    @Test
    fun `create throws NoSuchElementException when patient not found`() {
        every { patientDao.findById(1L) } returns null

        assertThrows<NoSuchElementException> { service.create(buildRequest()) }
        verify(exactly = 0) { appointmentDao.save(any()) }
    }

    @Test
    fun `create throws NoSuchElementException when specialist not found`() {
        every { patientDao.findById(1L) } returns buildPatient()
        every { specialistDao.findById(1L) } returns null

        assertThrows<NoSuchElementException> { service.create(buildRequest()) }
        verify(exactly = 0) { appointmentDao.save(any()) }
    }

    // updateStatus

    @Test
    fun `updateStatus changes appointment status`() {
        val appointment = buildAppointment(status = AppointmentStatusEnum.BOOKED)
        val updated = appointment.copy(status = AppointmentStatusEnum.CONFIRMED)
        every { appointmentDao.findById(1L) } returns appointment
        every { appointmentDao.save(any()) } returns updated

        val result = service.updateStatus(1L, AppointmentStatusRequest("CONFIRMED"))

        assertEquals("CONFIRMED", result.status)
    }

    @Test
    fun `updateStatus throws NoSuchElementException when appointment not found`() {
        every { appointmentDao.findById(99L) } returns null

        assertThrows<NoSuchElementException> {
            service.updateStatus(99L, AppointmentStatusRequest("COMPLETED"))
        }
    }

    @Test
    fun `updateStatus throws IllegalArgumentException for invalid status`() {
        every { appointmentDao.findById(1L) } returns buildAppointment()

        assertThrows<IllegalArgumentException> {
            service.updateStatus(1L, AppointmentStatusRequest("INVALID_STATUS"))
        }
    }

    // delete

    @Test
    fun `delete calls deleteById when appointment exists`() {
        every { appointmentDao.findById(1L) } returns buildAppointment()
        every { appointmentDao.deleteById(1L) } just runs

        service.delete(1L)

        verify { appointmentDao.deleteById(1L) }
    }

    @Test
    fun `delete throws NoSuchElementException when appointment not found`() {
        every { appointmentDao.findById(99L) } returns null

        assertThrows<NoSuchElementException> { service.delete(99L) }
        verify(exactly = 0) { appointmentDao.deleteById(any()) }
    }
}
