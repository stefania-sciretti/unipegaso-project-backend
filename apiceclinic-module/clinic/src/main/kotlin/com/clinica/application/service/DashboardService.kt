package com.clinica.application.service

import com.clinica.doors.outbound.database.repositories.AppointmentRepository
import com.clinica.doors.outbound.database.repositories.PatientRepository
import com.clinica.dto.AppointmentsByMonth
import com.clinica.dto.DashboardStatsResponse
import com.clinica.dto.KpiStats
import com.clinica.dto.RevenueByMonth
import com.clinica.dto.RevenueByService
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class DashboardService(
    private val patientRepository: PatientRepository,
    private val appointmentRepository: AppointmentRepository,
) {
    fun getDashboard(): DashboardStatsResponse {
        val now = LocalDateTime.now()
        val startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0)
        val startOfPrevMonth = startOfMonth.minusMonths(1)
        val startOf12MonthsAgo = startOfMonth.minusMonths(11)
        val monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM")

        val allAppointments = appointmentRepository.findAll()
        val thisMonthAppts = allAppointments.filter { it.scheduledAt >= startOfMonth }
        val prevMonthAppts = allAppointments.filter { it.scheduledAt >= startOfPrevMonth && it.scheduledAt < startOfMonth }

        // KPI
        val revenueMonth = thisMonthAppts.filter { it.status == "COMPLETED" }.sumOf { it.price }.toDouble()
        val revenuePrevMonth = prevMonthAppts.filter { it.status == "COMPLETED" }.sumOf { it.price }.toDouble()
        val activePatients = thisMonthAppts
            .filter { it.status in listOf("BOOKED", "CONFIRMED") }
            .map { it.patientEntity.id }
            .distinct()
            .count()
            .toLong()
        val newPatients = patientRepository.countByCreatedAtBetween(startOfMonth, now)
        val appointmentsMonth = thisMonthAppts.size.toLong()
        val cancellationRate = if (appointmentsMonth > 0)
            thisMonthAppts.count { it.status == "CANCELLED" }.toDouble() / appointmentsMonth * 100.0
        else 0.0
        val agendaOccupancy = if (appointmentsMonth > 0)
            thisMonthAppts.count { it.status in listOf("COMPLETED", "CONFIRMED") }.toDouble() / appointmentsMonth * 100.0
        else 0.0

        // Revenue by month (last 12)
        val last12Appts = allAppointments.filter { it.scheduledAt >= startOf12MonthsAgo }
        val revenueByMonth = last12Appts
            .filter { it.status == "COMPLETED" }
            .groupBy { it.scheduledAt.format(monthFormatter) }
            .map { (month, appts) -> RevenueByMonth(month, appts.sumOf { it.price }.toDouble()) }
            .sortedBy { it.month }

        // Appointments by month (last 12)
        val appointmentsByMonth = last12Appts
            .groupBy { it.scheduledAt.format(monthFormatter) }
            .map { (month, appts) ->
                AppointmentsByMonth(
                    month = month,
                    booked = appts.count { it.status == "BOOKED" }.toLong(),
                    completed = appts.count { it.status == "COMPLETED" }.toLong(),
                    cancelled = appts.count { it.status == "CANCELLED" }.toLong()
                )
            }
            .sortedBy { it.month }

        // Revenue by service (all time, COMPLETED)
        val revenueByService = allAppointments
            .filter { it.status == "COMPLETED" }
            .groupBy { it.visitType }
            .map { (service, appts) -> RevenueByService(service, appts.sumOf { it.price }.toDouble()) }
            .sortedByDescending { it.total }

        return DashboardStatsResponse(
            kpi = KpiStats(
                revenueMonth = revenueMonth,
                revenuePrevMonth = revenuePrevMonth,
                activePatients = activePatients,
                newPatients = newPatients,
                appointmentsMonth = appointmentsMonth,
                cancellationRate = cancellationRate,
                agendaOccupancy = agendaOccupancy
            ),
            revenueByMonth = revenueByMonth,
            appointmentsByMonth = appointmentsByMonth,
            revenueByService = revenueByService
        )
    }
}

