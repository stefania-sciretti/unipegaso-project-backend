package com.clinica.application.service

import com.clinic.model.ReportRequest
import com.clinica.application.domain.Appointment
import com.clinica.application.domain.AppointmentStatusEnum
import com.clinica.application.domain.FitnessAppointment
import com.clinica.application.domain.Patient
import com.clinica.application.domain.Report
import com.clinica.application.domain.Specialist
import com.clinica.doors.outbound.database.dao.FitnessAppointmentDao
import com.clinica.doors.outbound.database.dao.ReportDao
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
class ReportServiceTest {

    @MockK private lateinit var reportDao: ReportDao
    @MockK private lateinit var fitnessAppointmentDao: FitnessAppointmentDao

    @InjectMockKs
    private lateinit var service: ReportService

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

    private fun buildAppointment(id: Long = 5L) = Appointment(
        id = id, patient = buildPatient(), specialist = buildSpecialist(),
        scheduledAt = fixedTime, visitType = "Personal Training",
        status = AppointmentStatusEnum.COMPLETED, notes = null, updatedAt = fixedTime
    )

    private fun buildFitnessAppointment(
        id: Long = 5L,
        status: AppointmentStatusEnum = AppointmentStatusEnum.COMPLETED
    ) = FitnessAppointment(
        id = id, patient = buildPatient(), specialist = buildSpecialist(),
        scheduledAt = fixedTime, serviceType = "Personal Training",
        status = status, notes = null, updatedAt = fixedTime
    )

    private fun buildReport(id: Long = 1L) = Report(
        id = id,
        appointment = buildAppointment(),
        issuedDate = LocalDate.of(2025, 6, 15),
        diagnosis = "Healthy",
        prescription = "Rest",
        specialistNotes = "Good progress",
        createdAt = fixedTime,
        updatedAt = fixedTime
    )

    private fun buildRequest(appointmentId: Long = 5L) = ReportRequest(
        appointmentId = appointmentId,
        diagnosis = "Healthy",
        prescription = "Rest",
        specialistNotes = "Good progress"
    )

    // ── findAll ────────────────────────────────────────────────────────────────

    @Test
    fun `findAll returns all reports`() {
        every { reportDao.findAll() } returns listOf(buildReport(1L), buildReport(2L))

        val result = service.findAll()

        assertEquals(2, result.size)
        verify { reportDao.findAll() }
    }

    @Test
    fun `findAll returns empty list when no reports exist`() {
        every { reportDao.findAll() } returns emptyList()

        val result = service.findAll()

        assertEquals(0, result.size)
        verify { reportDao.findAll() }
    }

    // ── findById ───────────────────────────────────────────────────────────────

    @Test
    fun `findById returns report when found`() {
        every { reportDao.findById(1L) } returns buildReport(1L)

        val result = service.findById(1L)

        assertEquals(1L, result.id)
        assertEquals("Healthy", result.diagnosis)
        assertEquals(5L, result.appointment.id)
    }

    @Test
    fun `findById throws NoSuchElementException when not found`() {
        every { reportDao.findById(99L) } returns null

        val ex = assertThrows<NoSuchElementException> { service.findById(99L) }
        assert(ex.message!!.contains("99"))
    }

    // ── findByAppointmentId ────────────────────────────────────────────────────

    @Test
    fun `findByAppointmentId returns report when found`() {
        every { reportDao.findByAppointmentId(5L) } returns buildReport()

        val result = service.findByAppointmentId(5L)

        assertEquals(5L, result.appointment.id)
    }

    @Test
    fun `findByAppointmentId throws NoSuchElementException when not found`() {
        every { reportDao.findByAppointmentId(99L) } returns null

        val ex = assertThrows<NoSuchElementException> { service.findByAppointmentId(99L) }
        assert(ex.message!!.contains("99"))
    }

    // ── create ─────────────────────────────────────────────────────────────────

    @Test
    fun `create saves and returns report when appointment is COMPLETED`() {
        val fitnessAppt = buildFitnessAppointment(status = AppointmentStatusEnum.COMPLETED)
        val saved = buildReport(id = 10L)
        every { fitnessAppointmentDao.findById(5L) } returns fitnessAppt
        every { reportDao.findByAppointmentId(5L) } returns null
        every { reportDao.save(any()) } returns saved

        val result = service.create(buildRequest())

        assertEquals(10L, result.id)
        assertEquals("Healthy", result.diagnosis)
        verify { reportDao.save(any()) }
    }

    @Test
    fun `create maps FitnessAppointment fields to Appointment correctly`() {
        val fitnessAppt = buildFitnessAppointment(id = 7L, status = AppointmentStatusEnum.COMPLETED)
        val saved = buildReport()
        every { fitnessAppointmentDao.findById(7L) } returns fitnessAppt
        every { reportDao.findByAppointmentId(7L) } returns null
        every { reportDao.save(any()) } returns saved

        service.create(buildRequest(appointmentId = 7L))

        verify {
            reportDao.save(withArg { report ->
                assertEquals(7L, report.appointment.id)
                assertEquals("Personal Training", report.appointment.visitType)
                assertEquals(1L, report.appointment.patient.id)
                assertEquals(10L, report.appointment.specialist.id)
            })
        }
    }

    @Test
    fun `create throws NoSuchElementException when appointment not found`() {
        every { fitnessAppointmentDao.findById(99L) } returns null

        assertThrows<NoSuchElementException> { service.create(buildRequest(appointmentId = 99L)) }
        verify(exactly = 0) { reportDao.save(any()) }
    }

    @Test
    fun `create throws IllegalStateException when appointment is not COMPLETED`() {
        val bookedAppt = buildFitnessAppointment(status = AppointmentStatusEnum.BOOKED)
        every { fitnessAppointmentDao.findById(5L) } returns bookedAppt

        val ex = assertThrows<IllegalStateException> { service.create(buildRequest()) }
        assert(ex.message!!.contains("COMPLETED"))
        verify(exactly = 0) { reportDao.save(any()) }
    }

    @Test
    fun `create throws IllegalStateException when report already exists for appointment`() {
        val fitnessAppt = buildFitnessAppointment(status = AppointmentStatusEnum.COMPLETED)
        every { fitnessAppointmentDao.findById(5L) } returns fitnessAppt
        every { reportDao.findByAppointmentId(5L) } returns buildReport()

        val ex = assertThrows<IllegalStateException> { service.create(buildRequest()) }
        assert(ex.message!!.contains("already exists"))
        verify(exactly = 0) { reportDao.save(any()) }
    }

    // ── update ─────────────────────────────────────────────────────────────────

    @Test
    fun `update saves report with updated fields`() {
        val existing = buildReport(1L).copy(
            diagnosis = "Old diagnosis",
            prescription = "Old prescription",
            specialistNotes = "Old notes"
        )
        every { reportDao.findById(1L) } returns existing
        every { reportDao.save(any()) } returns existing

        service.update(1L, buildRequest())

        verify {
            reportDao.save(withArg { report ->
                assertEquals("Healthy", report.diagnosis)
                assertEquals("Rest", report.prescription)
                assertEquals("Good progress", report.specialistNotes)
            })
        }
    }

    @Test
    fun `update preserves appointment and issuedDate from existing report`() {
        val existing = buildReport(1L)
        every { reportDao.findById(1L) } returns existing
        every { reportDao.save(any()) } returns existing

        service.update(1L, buildRequest())

        verify {
            reportDao.save(withArg { report ->
                assertEquals(existing.appointment.id, report.appointment.id)
                assertEquals(existing.issuedDate, report.issuedDate)
            })
        }
    }

    @Test
    fun `update throws NoSuchElementException when report not found`() {
        every { reportDao.findById(99L) } returns null

        val ex = assertThrows<NoSuchElementException> { service.update(99L, buildRequest()) }
        assert(ex.message!!.contains("99"))
        verify(exactly = 0) { reportDao.save(any()) }
    }
}
