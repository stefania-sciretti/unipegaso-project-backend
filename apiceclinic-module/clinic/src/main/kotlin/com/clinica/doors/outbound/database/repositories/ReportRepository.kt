package com.clinica.doors.outbound.database.repositories

import com.clinica.doors.outbound.database.entities.ReportEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ReportRepository : JpaRepository<ReportEntity, Long> {
    fun findByAppointmentEntityId(appointmentId: Long): ReportEntity?
    fun existsByAppointmentEntityId(appointmentId: Long): Boolean

    @Query("SELECT r.appointmentEntity.id FROM ReportEntity r WHERE r.appointmentEntity.id IN :ids")
    fun findAppointmentIdsWithReports(@Param("ids") ids: List<Long>): List<Long>
}
