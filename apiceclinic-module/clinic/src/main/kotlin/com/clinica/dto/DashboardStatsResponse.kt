package com.clinica.dto

data class DashboardStatsResponse(
    val kpi: KpiStats,
    val revenueByMonth: List<RevenueByMonth>,
    val appointmentsByMonth: List<AppointmentsByMonth>,
    val revenueByService: List<RevenueByService>
)

data class KpiStats(
    val revenueMonth: Double,
    val revenuePrevMonth: Double,
    val activePatients: Long,
    val newPatients: Long,
    val appointmentsMonth: Long,
    val cancellationRate: Double,
    val agendaOccupancy: Double
)

data class RevenueByMonth(val month: String, val total: Double)
data class AppointmentsByMonth(val month: String, val booked: Long, val completed: Long, val cancelled: Long)
data class RevenueByService(val service: String, val total: Double)
