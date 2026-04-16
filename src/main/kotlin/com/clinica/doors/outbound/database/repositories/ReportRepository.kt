package com.clinica.repository

import com.clinica.domain.Report
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReportRepository : JpaRepository<Report, Long> {
    fun findByAppointmentId(appointmentId: Long): Report?
    fun existsByAppointmentId(appointmentId: Long): Boolean
}
