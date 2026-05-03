package com.clinica.application.service

import com.clinica.doors.outbound.database.dao.DashboardDao
import com.clinica.dto.*
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class DashboardServiceTest {

    @MockK private lateinit var dashboardDao: DashboardDao

    @InjectMockKs
    private lateinit var service: DashboardService

    private fun buildStats(revenueMonth: Double = 160.0) = DashboardStatsResponse(
        kpi = KpiStats(
            revenueMonth = revenueMonth,
            revenuePrevMonth = 0.0,
            activePatients = 2L,
            newPatients = 3L,
            appointmentsMonth = 5L,
            cancellationRate = 20.0,
            agendaOccupancy = 60.0
        ),
        revenueByMonth = listOf(RevenueByMonth("2025-01", revenueMonth)),
        appointmentsByMonth = listOf(AppointmentsByMonth("2025-01", 1L, 2L, 1L)),
        revenueByArea = listOf(
            RevenueByArea(AreaInfo(1L, "Alimentazione"), 200.0),
            RevenueByArea(AreaInfo(2L, "Sport"), 150.0),
            RevenueByArea(AreaInfo(3L, "Clinica"), 300.0)
        )
    )

    @Test
    fun `getDashboard with default period 6m calls dao with 6 months`() {
        val stats = buildStats()
        every { dashboardDao.getDashboardStats(6) } returns stats

        val result = service.getDashboard()

        assertEquals(stats, result)
    }

    @Test
    fun `getDashboard with period 1m calls dao with 1 month`() {
        val stats = buildStats()
        every { dashboardDao.getDashboardStats(1) } returns stats

        val result = service.getDashboard("1m")

        assertEquals(stats, result)
    }

    @Test
    fun `getDashboard with period 3m calls dao with 3 months`() {
        val stats = buildStats()
        every { dashboardDao.getDashboardStats(3) } returns stats

        val result = service.getDashboard("3m")

        assertEquals(stats, result)
    }

    @Test
    fun `getDashboard with period 1y calls dao with 12 months`() {
        val stats = buildStats()
        every { dashboardDao.getDashboardStats(12) } returns stats

        val result = service.getDashboard("1y")

        assertEquals(stats, result)
    }

    @Test
    fun `getDashboard throws IllegalArgumentException for invalid period`() {
        val ex = assertThrows<IllegalArgumentException> { service.getDashboard("invalid") }
        assert(ex.message!!.contains("invalid"))
    }

    @Test
    fun `getDashboard returns empty stats when dao returns zeros`() {
        val stats = DashboardStatsResponse(
            kpi = KpiStats(0.0, 0.0, 0L, 0L, 0L, 0.0, 0.0),
            revenueByMonth = emptyList(),
            appointmentsByMonth = emptyList(),
            revenueByArea = emptyList()
        )
        every { dashboardDao.getDashboardStats(6) } returns stats

        val result = service.getDashboard()

        assertEquals(0.0, result.kpi.revenueMonth)
        assertEquals(0L, result.kpi.appointmentsMonth)
        assertEquals(0, result.revenueByMonth.size)
        assertEquals(0, result.revenueByArea.size)
    }
}
