package com.clinica.doors.outbound.database.repositories

import com.clinica.doors.outbound.database.entities.AppointmentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface AppointmentRepository : JpaRepository<AppointmentEntity, Long> {

    fun countByStatus(status: String): Long

    @Query(
        """
        SELECT a
        FROM AppointmentEntity a
        WHERE (:patientId IS NULL OR a.patientEntity.id = :patientId)
          AND (:specialistId  IS NULL OR a.specialistEntity.id = :specialistId)
          AND (:status    IS NULL OR a.status = :status)
        ORDER BY a.patientEntity.lastName
        """
    )
    fun search(
        @Param("patientId") patientId: Long?,
        @Param("specialistId") specialistId: Long?,
        @Param("status") status: String?
    ): List<AppointmentEntity>
}