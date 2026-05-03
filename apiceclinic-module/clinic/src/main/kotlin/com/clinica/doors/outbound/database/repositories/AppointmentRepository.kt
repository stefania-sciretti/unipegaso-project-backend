package com.clinica.doors.outbound.database.repositories

import com.clinica.doors.outbound.database.entities.AppointmentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDateTime

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

    @Query("""
        SELECT COALESCE(SUM(a.price), 0)
        FROM AppointmentEntity a
        WHERE a.status = :status
          AND a.scheduledAt >= :from
          AND a.scheduledAt < :to
    """)
    fun sumPriceByStatusBetween(
        @Param("status") status: String,
        @Param("from") from: LocalDateTime,
        @Param("to") to: LocalDateTime
    ): BigDecimal

    @Query("""
        SELECT COUNT(DISTINCT a.patientEntity.id)
        FROM AppointmentEntity a
        WHERE a.status IN :statuses
          AND a.scheduledAt >= :from
          AND a.scheduledAt < :to
    """)
    fun countDistinctPatientsByStatusIn(
        @Param("statuses") statuses: List<String>,
        @Param("from") from: LocalDateTime,
        @Param("to") to: LocalDateTime
    ): Long

    @Query("""
        SELECT COUNT(a)
        FROM AppointmentEntity a
        WHERE a.scheduledAt >= :from
          AND a.scheduledAt < :to
    """)
    fun countByScheduledAtBetween(
        @Param("from") from: LocalDateTime,
        @Param("to") to: LocalDateTime
    ): Long

    @Query("""
        SELECT COUNT(a)
        FROM AppointmentEntity a
        WHERE a.status = :status
          AND a.scheduledAt >= :from
          AND a.scheduledAt < :to
    """)
    fun countByStatusBetween(
        @Param("status") status: String,
        @Param("from") from: LocalDateTime,
        @Param("to") to: LocalDateTime
    ): Long

    @Query("""
        SELECT a
        FROM AppointmentEntity a
        WHERE a.scheduledAt >= :from
        ORDER BY a.scheduledAt
    """)
    fun findAllFromDate(@Param("from") from: LocalDateTime): List<AppointmentEntity>
}