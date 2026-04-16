package com.clinica

import com.clinica.domain.*
import com.clinica.dto.*
import com.clinica.repository.AppointmentRepository
import com.clinica.repository.DoctorRepository
import com.clinica.repository.PatientRepository
import com.clinica.repository.ReportRepository
import com.clinica.service.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.mockito.kotlin.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Optional

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class PatientServiceTest {

    private val patientRepository: PatientRepository = mock()
    private val patientService = PatientService(patientRepository)

    private fun samplePatient(id: Long = 1L) = PatientEntity(
        id = id,
        firstName = "John",
        lastName = "Doe",
        fiscalCode = "DOEJHN80A01H501Z",
        birthDate = LocalDate.of(1980, 1, 1),
        email = "john.doe@email.com",
        phone = "3331234567"
    )

    private fun sampleRequest() = PatientRequest(
        firstName = "John",
        lastName = "Doe",
        fiscalCode = "DOEJHN80A01H501Z",
        birthDate = LocalDate.of(1980, 1, 1),
        email = "john.doe@email.com",
        phone = "3331234567"
    )

    @Test
    @Order(1)
    fun `findAll returns list of patients`() {
        whenever(patientRepository.findAll()).thenReturn(listOf(samplePatient()))
        val result = patientService.findAll()
        assertEquals(1, result.size)
        assertEquals("John", result[0].firstName)
    }

    @Test
    @Order(2)
    fun `findById returns patient when exists`() {
        whenever(patientRepository.findById(1L)).thenReturn(Optional.of(samplePatient()))
        val result = patientService.findById(1L)
        assertEquals("Doe", result.lastName)
        assertEquals("DOEJHN80A01H501Z", result.fiscalCode)
    }

    @Test
    @Order(3)
    fun `findById throws when not found`() {
        whenever(patientRepository.findById(99L)).thenReturn(Optional.empty())
        assertThrows<NoSuchElementException> { patientService.findById(99L) }
    }

    @Test
    @Order(4)
    fun `create saves and returns patient`() {
        val req = sampleRequest()
        whenever(patientRepository.existsByFiscalCode(req.fiscalCode)).thenReturn(false)
        whenever(patientRepository.save(any())).thenReturn(samplePatient())
        val result = patientService.create(req)
        assertEquals("John", result.firstName)
        verify(patientRepository).save(any())
    }

    @Test
    @Order(5)
    fun `create throws when fiscal code already exists`() {
        val req = sampleRequest()
        whenever(patientRepository.existsByFiscalCode(req.fiscalCode)).thenReturn(true)
        assertThrows<IllegalArgumentException> { patientService.create(req) }
        verify(patientRepository, never()).save(any())
    }

    @Test
    @Order(6)
    fun `delete removes patient`() {
        whenever(patientRepository.existsById(1L)).thenReturn(true)
        patientService.delete(1L)
        verify(patientRepository).deleteById(1L)
    }

    @Test
    @Order(7)
    fun `delete throws when patient not found`() {
        whenever(patientRepository.existsById(99L)).thenReturn(false)
        assertThrows<NoSuchElementException> { patientService.delete(99L) }
        verify(patientRepository, never()).deleteById(any())
    }
}

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class AppointmentServiceTest {

    private val appointmentRepository: AppointmentRepository = mock()
    private val patientRepository: PatientRepository = mock()
    private val doctorRepository: DoctorRepository = mock()
    private val appointmentService = AppointmentService(appointmentRepository, patientRepository, doctorRepository)

    private fun samplePatient() = PatientEntity(
        id = 1L, firstName = "John", lastName = "Doe",
        fiscalCode = "DOEJHN80A01H501Z", birthDate = LocalDate.of(1980, 1, 1),
        email = "john@email.com"
    )

    private fun sampleDoctor() = Doctor(
        id = 1L, firstName = "Laura", lastName = "Smith",
        specialization = "Cardiology", email = "l.smith@clinic.it",
        licenseNumber = "RM-12345"
    )

    private fun sampleAppointment() = AppointmentEntity(
        id = 1L,
        patientEntity = samplePatient(),
        doctor = sampleDoctor(),
        scheduledAt = LocalDateTime.now().plusDays(2),
        visitType = "Cardiology Check-up",
        status = AppointmentStatus.BOOKED
    )

    @Test
    @Order(1)
    fun `create books new appointment`() {
        val req = AppointmentRequest(
            patientId = 1L, doctorId = 1L,
            scheduledAt = LocalDateTime.now().plusDays(2),
            visitType = "Cardiology Check-up"
        )
        whenever(patientRepository.findById(1L)).thenReturn(Optional.of(samplePatient()))
        whenever(doctorRepository.findById(1L)).thenReturn(Optional.of(sampleDoctor()))
        whenever(appointmentRepository.save(any())).thenReturn(sampleAppointment())
        val result = appointmentService.create(req)
        assertEquals("BOOKED", result.status)
        assertEquals("John Doe", result.patientFullName)
    }

    @Test
    @Order(2)
    fun `updateStatus changes appointment status`() {
        val appt = sampleAppointment()
        whenever(appointmentRepository.findById(1L)).thenReturn(Optional.of(appt))
        whenever(appointmentRepository.save(any())).thenReturn(appt.also { it.status = AppointmentStatus.CONFIRMED })
        val result = appointmentService.updateStatus(1L, AppointmentStatusRequest("CONFIRMED"))
        assertEquals("CONFIRMED", result.status)
    }

    @Test
    @Order(3)
    fun `updateStatus throws on invalid status string`() {
        whenever(appointmentRepository.findById(1L)).thenReturn(Optional.of(sampleAppointment()))
        assertThrows<IllegalArgumentException> {
            appointmentService.updateStatus(1L, AppointmentStatusRequest("INVALID"))
        }
    }

    @Test
    @Order(4)
    fun `delete sets status to CANCELLED`() {
        val appt = sampleAppointment()
        whenever(appointmentRepository.findById(1L)).thenReturn(Optional.of(appt))
        whenever(appointmentRepository.save(any())).thenReturn(appt)
        appointmentService.delete(1L)
        verify(appointmentRepository).save(argThat { status == AppointmentStatus.CANCELLED })
    }
}

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ReportServiceTest {

    private val reportRepository: ReportRepository = mock()
    private val appointmentRepository: AppointmentRepository = mock()
    private val reportService = ReportService(reportRepository, appointmentRepository)

    private fun completedAppointment() = AppointmentEntity(
        id = 3L,
        patientEntity = PatientEntity(
            id = 1L, firstName = "Matteo", lastName = "Esposito",
            fiscalCode = "SPSMT85C10L219K", birthDate = LocalDate.of(1985, 3, 10),
            email = "matteo@email.it"
        ),
        doctor = Doctor(
            id = 3L, firstName = "Giuseppe", lastName = "Verdi",
            specialization = "Ortopedia", email = "g.verdi@clinic.it",
            licenseNumber = "NA-11223"
        ),
        scheduledAt = LocalDateTime.now().minusDays(5),
        visitType = "Ortopedica",
        status = AppointmentStatus.COMPLETED
    )

    @Test
    @Order(1)
    fun `create report for completed appointment`() {
        val appt = completedAppointment()
        val req = ReportRequest(appointmentId = 3L, diagnosis = "Tendinite")
        whenever(appointmentRepository.findById(3L)).thenReturn(Optional.of(appt))
        whenever(reportRepository.existsByAppointmentId(3L)).thenReturn(false)
        val report = Report(id = 1L, appointmentEntity = appt, diagnosis = "Tendinite")
        whenever(reportRepository.save(any())).thenReturn(report)
        val result = reportService.create(req)
        assertEquals("Tendinite", result.diagnosis)
        assertEquals("Matteo Esposito", result.patientFullName)
    }

    @Test
    @Order(2)
    fun `create report throws when appointment not completed`() {
        val appt = completedAppointment().also { it.status = AppointmentStatus.BOOKED }
        whenever(appointmentRepository.findById(3L)).thenReturn(Optional.of(appt))
        val req = ReportRequest(appointmentId = 3L, diagnosis = "Test")
        assertThrows<IllegalStateException> { reportService.create(req) }
    }

    @Test
    @Order(3)
    fun `create report throws when report already exists`() {
        whenever(appointmentRepository.findById(3L)).thenReturn(Optional.of(completedAppointment()))
        whenever(reportRepository.existsByAppointmentId(3L)).thenReturn(true)
        val req = ReportRequest(appointmentId = 3L, diagnosis = "Duplicate")
        assertThrows<IllegalStateException> { reportService.create(req) }
    }
}
