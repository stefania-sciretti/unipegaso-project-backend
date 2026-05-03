package com.clinica.application.service

import com.clinica.doors.outbound.database.entities.AppointmentEntity
import com.clinica.doors.outbound.database.entities.PatientEntity
import com.clinica.doors.outbound.database.entities.SpecialistEntity
import com.clinica.doors.outbound.database.repositories.AppointmentRepository
import com.clinica.doors.outbound.database.repositories.PatientRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class DashboardServiceTest {

    @MockK private lateinit var patientRepository: PatientRepository
    @MockK private lateinit var appointmentRepository: AppointmentRepository

    @InjectMockKs
    private lateinit var service: DashboardService

    // ── helpers ──────────────────────────────────────────────────────────────

    private fun makePatient(id: Long): PatientEntity = PatientEntity(
        id = id,
        firstName = "Mario", lastName = "Rossi",
        fiscalCode = "RSSMRA80A01H501Z",
        birthDate = LocalDate.of(1980, 1, 1),
        email = "mario.rossi@test.it",
        phone = null,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )

    private fun makeSpecialist(): SpecialistEntity = SpecialistEntity(
        id = 1L, firstName = "Dr", lastName = "House",
        role = "NUTRITIONIST", email = "house@test.it", bio = ""
    )

    private fun makeAppointment(
        id: Long,
        patientId: Long,
        scheduledAt: LocalDateTime,
        status: String,
        visitType: String = "Visita generica",
        price: BigDecimal = BigDecimal("100.00")
    ): AppointmentEntity = AppointmentEntity(
        id = id,
        patientEntity = makePatient(patientId),
        specialistEntity = makeSpecialist(),
        scheduledAt = scheduledAt,
        visitType = visitType,
        status = status,
        notes = null,
        price = price,
        updatedAt = scheduledAt
    )

    // ── tests ─────────────────────────────────────────────────────────────────

    @Test
    fun `getDashboard returns all zeros when no appointments exist`() {
        every { appointmentRepository.findAll() } returns emptyList()
        every { patientRepository.countByCreatedAtBetween(any(), any()) } returns 0L

        val result = service.getDashboard()

        assertEquals(0.0, result.kpi.revenueMonth)
        assertEquals(0.0, result.kpi.revenuePrevMonth)
        assertEquals(0L, result.kpi.activePatients)
        assertEquals(0L, result.kpi.newPatients)
        assertEquals(0L, result.kpi.appointmentsMonth)
        assertEquals(0.0, result.kpi.cancellationRate)
        assertEquals(0.0, result.kpi.agendaOccupancy)
        assertTrue(result.revenueByMonth.isEmpty())
        assertTrue(result.appointmentsByMonth.isEmpty())
        assertTrue(result.revenueByService.isEmpty())
    }

    @Test
    fun `getDashboard correctly computes KPI from mixed statuses this month`() {
        val now = LocalDateTime.now()
        val thisMonth = now.withDayOfMonth(1).plusDays(1)

        val appointments = listOf(
            makeAppointment(1, 1, thisMonth, "COMPLETED", "Visita dietologica", BigDecimal("80.00")),
            makeAppointment(2, 2, thisMonth, "COMPLETED", "Visita dietologica", BigDecimal("80.00")),
            makeAppointment(3, 3, thisMonth, "BOOKED",    "Personal training",  BigDecimal("60.00")),
            makeAppointment(4, 4, thisMonth, "CONFIRMED", "Visita medica",      BigDecimal("120.00")),
            makeAppointment(5, 5, thisMonth, "CANCELLED", "Visita ortopedica",  BigDecimal("100.00"))
        )

        every { appointmentRepository.findAll() } returns appointments
        every { patientRepository.countByCreatedAtBetween(any(), any()) } returns 3L

        val result = service.getDashboard()

        // revenueMonth = 80 + 80 = 160 (only COMPLETED)
        assertEquals(160.0, result.kpi.revenueMonth)
        // revenuePrevMonth = 0 (no appointments last month)
        assertEquals(0.0, result.kpi.revenuePrevMonth)
        // activePatients: BOOKED(id=3) + CONFIRMED(id=4) → 2 distinct patients
        assertEquals(2L, result.kpi.activePatients)
        // newPatients from mock
        assertEquals(3L, result.kpi.newPatients)
        // appointmentsMonth = 5
        assertEquals(5L, result.kpi.appointmentsMonth)
        // cancellationRate = 1/5 * 100 = 20.0
        assertEquals(20.0, result.kpi.cancellationRate, 0.001)
        // agendaOccupancy = (COMPLETED=2 + CONFIRMED=1) / 5 * 100 = 60.0
        assertEquals(60.0, result.kpi.agendaOccupancy, 0.001)
    }

    @Test
    fun `getDashboard computes revenueByMonth only for COMPLETED appointments`() {
        val now = LocalDateTime.now()
        val thisMonth = now.withDayOfMonth(1).plusDays(1)
        val lastMonth = thisMonth.minusMonths(1)

        val appointments = listOf(
            makeAppointment(1, 1, thisMonth,  "COMPLETED", "Visita A", BigDecimal("100.00")),
            makeAppointment(2, 2, thisMonth,  "CANCELLED", "Visita B", BigDecimal("90.00")),
            makeAppointment(3, 3, lastMonth,  "COMPLETED", "Visita A", BigDecimal("80.00")),
        )

        every { appointmentRepository.findAll() } returns appointments
        every { patientRepository.countByCreatedAtBetween(any(), any()) } returns 0L

        val result = service.getDashboard()

        // Only COMPLETED entries appear in revenueByMonth
        assertEquals(2, result.revenueByMonth.size)
        val totals = result.revenueByMonth.associate { it.month to it.total }
        assertEquals(100.0, totals[thisMonth.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"))])
        assertEquals(80.0, totals[lastMonth.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"))])
    }

    @Test
    fun `getDashboard computes appointmentsByMonth with correct counts per status`() {
        val now = LocalDateTime.now()
        val thisMonth = now.withDayOfMonth(1).plusDays(1)

        val appointments = listOf(
            makeAppointment(1, 1, thisMonth, "BOOKED",    price = BigDecimal("60.00")),
            makeAppointment(2, 2, thisMonth, "COMPLETED", price = BigDecimal("100.00")),
            makeAppointment(3, 3, thisMonth, "COMPLETED", price = BigDecimal("100.00")),
            makeAppointment(4, 4, thisMonth, "CANCELLED", price = BigDecimal("80.00")),
        )

        every { appointmentRepository.findAll() } returns appointments
        every { patientRepository.countByCreatedAtBetween(any(), any()) } returns 0L

        val result = service.getDashboard()

        assertEquals(1, result.appointmentsByMonth.size)
        val entry = result.appointmentsByMonth.first()
        assertEquals(1L, entry.booked)
        assertEquals(2L, entry.completed)
        assertEquals(1L, entry.cancelled)
    }

    @Test
    fun `getDashboard computes revenueByService sorted descending by total`() {
        val now = LocalDateTime.now()
        val past = now.minusMonths(2)

        val appointments = listOf(
            makeAppointment(1, 1, past, "COMPLETED", "Fisioterapia",     BigDecimal("90.00")),
            makeAppointment(2, 2, past, "COMPLETED", "Fisioterapia",     BigDecimal("90.00")),
            makeAppointment(3, 3, past, "COMPLETED", "Visita medica",    BigDecimal("200.00")),
            makeAppointment(4, 4, past, "CANCELLED", "Visita dietologica", BigDecimal("80.00")),
        )

        every { appointmentRepository.findAll() } returns appointments
        every { patientRepository.countByCreatedAtBetween(any(), any()) } returns 0L

        val result = service.getDashboard()

        // CANCELLED should not appear in revenueByService
        assertEquals(2, result.revenueByService.size)
        assertEquals("Visita medica", result.revenueByService[0].service)
        assertEquals(200.0, result.revenueByService[0].total)
        assertEquals("Fisioterapia", result.revenueByService[1].service)
        assertEquals(180.0, result.revenueByService[1].total)
    }

    @Test
    fun `getDashboard cancellationRate is zero when no appointments this month`() {
        val past = LocalDateTime.now().minusMonths(2)
        val appointments = listOf(
            makeAppointment(1, 1, past, "COMPLETED", price = BigDecimal("100.00"))
        )

        every { appointmentRepository.findAll() } returns appointments
        every { patientRepository.countByCreatedAtBetween(any(), any()) } returns 0L

        val result = service.getDashboard()

        assertEquals(0.0, result.kpi.cancellationRate)
        assertEquals(0.0, result.kpi.agendaOccupancy)
        assertEquals(0L, result.kpi.appointmentsMonth)
    }
}
