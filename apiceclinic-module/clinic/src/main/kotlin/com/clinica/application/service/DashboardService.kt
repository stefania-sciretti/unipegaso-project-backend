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
    fun getDashboard(): DashboardStatsResponse =
        dashboardDao.getDashboardStats()
}

