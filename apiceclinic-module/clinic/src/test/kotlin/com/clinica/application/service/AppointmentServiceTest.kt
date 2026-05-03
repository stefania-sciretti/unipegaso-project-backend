package com.clinica.application.service

import com.clinic.model.AppointmentRequest
import com.clinic.model.AppointmentStatusRequest
import com.clinica.application.domain.Appointment
import com.clinica.application.domain.AppointmentStatusEnum
import com.clinica.application.domain.Patient
import com.clinica.application.domain.Specialist
import com.clinica.doors.outbound.database.dao.AppointmentDao
import com.clinica.doors.outbound.database.dao.PatientDao
import com.clinica.doors.outbound.database.dao.ReportDao
import com.clinica.doors.outbound.database.dao.SpecialistDao
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

@ExtendWith(MockKExtension::class)
class AppointmentServiceTest {

    @MockK private lateinit var appointmentDao: AppointmentDao
    @MockK private lateinit var patientDao: PatientDao
    @MockK private lateinit var specialistDao: SpecialistDao
    @MockK private lateinit var reportDao: ReportDao

    @InjectMockKs
    private lateinit var service: AppointmentService

    private val fixedTime = LocalDateTime.of(2025, 6, 15, 10, 0)

    private fun buildPatient(id: Long = 1L) = Patient(
        id = id, firstName = "Mario", lastName = "Rossi",
        fiscalCode = "RSSMRA80A01H501U", birthDate = LocalDate.of(1980, 1, 1),
        email = "mario@example.com"
    )

    private fun buildSpecialist(id: Long = 1L) = Specialist(
        id = id, firstName = "Luigi", lastName = "Bianchi",
        role = "PERSONAL_TRAINER", email = "luigi@example.com"
    )

    private fun buildAppointment(
        id: Long = 1L,
        status: AppointmentStatusEnum = AppointmentStatusEnum.BOOKED
    ) = Appointment(
        id = id, patient = buildPatient(), specialist = buildSpecialist(),
        scheduledAt = fixedTime, serviceType = "PERSONAL_TRAINING", status = status,
        notes = "Note", price = BigDecimal("50.00"), createdAt = fixedTime, updatedAt = fixedTime
    )

    private fun buildRequest() = AppointmentRequest(
        patientId = 1L, specialistId = 1L,
        scheduledAt = fixedTime.atOffset(ZoneOffset.UTC),
        serviceType = "PERSONAL_TRAINING", notes = "Note", price = 50.0
    )

    @Test
    fun `findAll returns mapped appointments`() {
        every { appointmentDao.findAll(null, null, null) } returns
            listOf(buildAppointment(1L), buildAppointment(2L))
        every { reportDao.findAppointmentIdsWithReports(any()) } returns emptySet()

        val result = service.findAll(null, null, null)

        assertEquals(2, result.size)
        assertEquals(1L, result[0].patient.id)
        assertEquals(false, result[0].hasReport)
    }

    @Test
    fun `findAll filters by patientId`() {
        every { appointmentDao.findAll(1L, null, null) } returns listOf(buildAppointment())
        every { reportDao.findAppointmentIdsWithReports(any()) } returns emptySet()

        val result = service.findAll(1L, null, null)

        assertEquals(1, result.size)
        verify { appointmentDao.findAll(1L, null, null) }
    }

    @Test
    fun `findAll parses status string to enum`() {
        every { appointmentDao.findAll(null, null, AppointmentStatusEnum.BOOKED) } returns listOf(buildAppointment())
        every { reportDao.findAppointmentIdsWithReports(any()) } returns emptySet()

        val result = service.findAll(null, null, "BOOKED")

        assertEquals(1, result.size)
        verify { appointmentDao.findAll(null, null, AppointmentStatusEnum.BOOKED) }
    }

    @Test
    fun `findAll sets hasReport true when report exists`() {
        every { appointmentDao.findAll(null, null, null) } returns listOf(buildAppointment(1L))
        every { reportDao.findAppointmentIdsWithReports(any()) } returns setOf(1L)

        val result = service.findAll(null, null, null)

        assertEquals(true, result[0].hasReport)
    }

    @Test
    fun `findById returns appointment when found`() {
        every { appointmentDao.findById(1L) } returns buildAppointment()
        every { reportDao.existsByAppointmentId(1L) } returns false

        val result = service.findById(1L)

        assertEquals(1L, result.id)
        assertEquals(AppointmentStatusEnum.BOOKED, result.status)
        assertEquals("PERSONAL_TRAINING", result.serviceType)
        assertEquals("Rossi Mario", result.patient.fullName)
        assertEquals(false, result.hasReport)
    }

    @Test
    fun `findById sets hasReport true when report exists`() {
        every { appointmentDao.findById(1L) } returns buildAppointment()
        every { reportDao.existsByAppointmentId(1L) } returns true

        val result = service.findById(1L)

        assertEquals(true, result.hasReport)
    }

    @Test
    fun `findById throws NoSuchElementException when not found`() {
        every { appointmentDao.findById(99L) } returns null

        val ex = assertThrows<NoSuchElementException> { service.findById(99L) }
        assert(ex.message!!.contains("99"))
    }

    @Test
    fun `create saves appointment with BOOKED status`() {
        val saved = buildAppointment(id = 5L)
        every { patientDao.findById(1L) } returns buildPatient()
        every { specialistDao.findById(1L) } returns buildSpecialist()
        every { appointmentDao.save(any()) } returns saved

        val result = service.create(buildRequest())

        assertEquals(5L, result.id)
        assertEquals(AppointmentStatusEnum.BOOKED, result.status)
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

    @Test
    fun `updateStatus changes appointment status`() {
        val appointment = buildAppointment(status = AppointmentStatusEnum.BOOKED)
        val updated = appointment.copy(status = AppointmentStatusEnum.CONFIRMED)
        every { appointmentDao.findById(1L) } returns appointment
        every { appointmentDao.save(any()) } returns updated
        every { reportDao.existsByAppointmentId(any()) } returns false

        val result = service.updateStatus(1L, AppointmentStatusRequest("CONFIRMED"))

        assertEquals("CONFIRMED", result.status.name)
    }

    @Test
    fun `updateStatus preserves hasReport=true when report exists`() {
        val appointment = buildAppointment(status = AppointmentStatusEnum.BOOKED)
        val updated = appointment.copy(status = AppointmentStatusEnum.COMPLETED)
        every { appointmentDao.findById(1L) } returns appointment
        every { appointmentDao.save(any()) } returns updated
        every { reportDao.existsByAppointmentId(1L) } returns true

        val result = service.updateStatus(1L, AppointmentStatusRequest("COMPLETED"))

        assertEquals(true, result.hasReport)
    }

    @Test
    fun `updateStatus throws NoSuchElementException when not found`() {
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

    @Test
    fun `delete soft-cancels appointment`() {
        val appointment = buildAppointment(status = AppointmentStatusEnum.BOOKED)
        val cancelled = appointment.copy(status = AppointmentStatusEnum.CANCELLED)
        every { appointmentDao.findById(1L) } returns appointment
        every { appointmentDao.save(any()) } returns cancelled

        service.delete(1L)

        verify {
            appointmentDao.save(withArg { it.status == AppointmentStatusEnum.CANCELLED })
        }
    }

    @Test
    fun `delete throws NoSuchElementException when not found`() {
        every { appointmentDao.findById(99L) } returns null

        assertThrows<NoSuchElementException> { service.delete(99L) }
        verify(exactly = 0) { appointmentDao.save(any()) }
    }
}
