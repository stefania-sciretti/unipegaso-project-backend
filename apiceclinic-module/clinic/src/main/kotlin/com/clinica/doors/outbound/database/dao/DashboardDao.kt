package com.clinica.doors.outbound.database.dao

import com.clinica.dto.AppointmentsByMonth
import com.clinica.dto.AreaInfo
import com.clinica.dto.DashboardStatsResponse
import com.clinica.dto.KpiStats
import com.clinica.dto.RevenueByArea
import com.clinica.dto.RevenueByMonth
import com.clinica.doors.outbound.database.entities.AppointmentEntity
import com.clinica.doors.outbound.database.repositories.AppointmentRepository
import com.clinica.doors.outbound.database.repositories.PatientRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class DashboardDao(
    private val appointmentRepository: AppointmentRepository,
    private val patientRepository: PatientRepository
) {
    private val monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM")

    @Transactional(readOnly = true)
    fun getDashboardStats(months: Int): DashboardStatsResponse {
        val now = LocalDateTime.now()
        val startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0)
        val startOfNextMonth = startOfMonth.plusMonths(1)
        val startOfPrevMonth = startOfMonth.minusMonths(1)
        val startOfPeriod = startOfMonth.minusMonths(months.toLong() - 1)

        val allFromDate = appointmentRepository.findAllFromDate(startOfPeriod)

        return DashboardStatsResponse(
            kpi = computeKpi(now, startOfMonth, startOfNextMonth, startOfPrevMonth),
            revenueByMonth = computeRevenueByMonth(allFromDate),
            appointmentsByMonth = computeAppointmentsByMonth(allFromDate),
            revenueByArea = computeRevenueByArea(allFromDate)
        )
    }

    private fun computeKpi(
        now: LocalDateTime,
        startOfMonth: LocalDateTime,
        startOfNextMonth: LocalDateTime,
        startOfPrevMonth: LocalDateTime
    ): KpiStats {
        val revenueMonth = appointmentRepository
            .sumPriceByStatusBetween("COMPLETED", startOfMonth, startOfNextMonth)
            .toDouble().round2()
        val revenuePrevMonth = appointmentRepository
            .sumPriceByStatusBetween("COMPLETED", startOfPrevMonth, startOfMonth)
            .toDouble().round2()
        val activePatients = appointmentRepository
            .countDistinctPatientsByStatusIn(listOf("BOOKED", "CONFIRMED"), startOfMonth, startOfNextMonth)
        val newPatients = patientRepository.countByCreatedAtBetween(startOfMonth, now)
        val appointmentsMonth = appointmentRepository
            .countByScheduledAtBetween(startOfMonth, startOfNextMonth)
        val cancelledMonth = appointmentRepository
            .countByStatusBetween("CANCELLED", startOfMonth, startOfNextMonth)
        val completedOrConfirmedMonth = appointmentRepository
            .countByStatusBetween("COMPLETED", startOfMonth, startOfNextMonth) +
            appointmentRepository.countByStatusBetween("CONFIRMED", startOfMonth, startOfNextMonth)

        val cancellationRate = if (appointmentsMonth > 0)
            (cancelledMonth.toDouble() / appointmentsMonth * 100.0).round2()
        else 0.0
        val agendaOccupancy = if (appointmentsMonth > 0)
            (completedOrConfirmedMonth.toDouble() / appointmentsMonth * 100.0).round2()
        else 0.0

        return KpiStats(
            revenueMonth = revenueMonth,
            revenuePrevMonth = revenuePrevMonth,
            activePatients = activePatients,
            newPatients = newPatients,
            appointmentsMonth = appointmentsMonth,
            cancellationRate = cancellationRate,
            agendaOccupancy = agendaOccupancy
        )
    }

    private fun computeRevenueByMonth(appointments: List<AppointmentEntity>): List<RevenueByMonth> =
        appointments
            .filter { it.status == "COMPLETED" }
            .groupBy { it.scheduledAt.format(monthFormatter) }
            .map { (month, appts) -> RevenueByMonth(month, appts.sumOf { it.price }.toDouble().round2()) }
            .sortedBy { it.month }

    private fun computeAppointmentsByMonth(appointments: List<AppointmentEntity>): List<AppointmentsByMonth> =
        appointments
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

    private fun computeRevenueByArea(appointments: List<AppointmentEntity>): List<RevenueByArea> =
        appointments
            .filter { it.status == "COMPLETED" && it.area != null }
            .groupBy { it.area!! }
            .map { (area, appts) ->
                RevenueByArea(
                    area = AreaInfo(areaId = area.id, areaName = area.name),
                    total = appts.sumOf { it.price }.toDouble().round2()
                )
            }
            .sortedBy { it.area.areaId }

    private fun Double.round2(): Double =
        BigDecimal(this).setScale(2, RoundingMode.HALF_UP).toDouble()
}
