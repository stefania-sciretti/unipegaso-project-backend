package com.clinica.application.service

import com.clinica.doors.outbound.database.dao.DashboardDao
import com.clinica.dto.DashboardStatsResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DashboardService(
    private val dashboardDao: DashboardDao
) {
    @Transactional(readOnly = true)
    fun getDashboard(period: String = "6m"): DashboardStatsResponse {
        val months = when (period) {
            "1m" -> 1
            "3m" -> 3
            "6m" -> 6
            "1y" -> 12
            else  -> throw IllegalArgumentException("Periodo non valido: '$period'. Valori accettati: 1m, 3m, 6m, 1y")
        }
        return dashboardDao.getDashboardStats(months)
    }
}

