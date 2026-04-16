package com.clinica.doors.outbound.database.repositories

import com.clinica.doors.outbound.database.entities.ReportEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReportRepository : JpaRepository<ReportEntity, Long> {

    fun findByAppointmentEntityId(appointmentId: Long): ReportEntity?
}