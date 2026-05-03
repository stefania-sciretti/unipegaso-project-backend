package com.clinica.application.service

import com.clinica.doors.outbound.database.dao.DashboardDao
import com.clinica.dto.*
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class DashboardServiceTest {

    @MockK private lateinit var dashboardDao: DashboardDao

    @InjectMockKs
    private lateinit var service: DashboardService

    @Test
    fun `getDashboard returns stats from dao`() {
        val stats = DashboardStatsResponse(
            kpi = KpiStats(
                revenueMonth = 160.0,
                revenuePrevMonth = 0.0,
                activePatients = 2L,
                newPatients = 3L,
                appointmentsMonth = 5L,
                cancellationRate = 20.0,
                agendaOccupancy = 60.0
            ),
            revenueByMonth = listOf(RevenueByMonth("2025-01", 160.0)),
            appointmentsByMonth = listOf(AppointmentsByMonth("2025-01", 1L, 2L, 1L)),
            revenueByService = listOf(RevenueByService("Visita medica", 200.0))
        )
        every { dashboardDao.getDashboardStats() } returns stats

        val result = service.getDashboard()

        assertEquals(stats, result)
    }

    @Test
    fun `getDashboard returns empty stats when dao returns zeros`() {
        val stats = DashboardStatsResponse(
            kpi = KpiStats(0.0, 0.0, 0L, 0L, 0L, 0.0, 0.0),
            revenueByMonth = emptyList(),
            appointmentsByMonth = emptyList(),
            revenueByService = emptyList()
        )
        every { dashboardDao.getDashboardStats() } returns stats

        val result = service.getDashboard()

        assertEquals(0.0, result.kpi.revenueMonth)
        assertEquals(0L, result.kpi.appointmentsMonth)
        assertEquals(0, result.revenueByMonth.size)
    }
}
