package com.clinica.doors.outbound.database.repositories

import com.clinica.doors.outbound.database.entities.FitnessAppointmentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface FitnessAppointmentRepository : JpaRepository<FitnessAppointmentEntity, Long> {

    fun findByClientEntityId(clientId: Long): List<FitnessAppointmentEntity>

    fun findByStaffId(trainerId: Long): List<FitnessAppointmentEntity>

    fun findByStatus(status: String): List<FitnessAppointmentEntity>

    fun findByClientEntityIdAndStaffId(
        clientId: Long,
        trainerId: Long
    ): List<FitnessAppointmentEntity>

    fun findByClientEntityIdAndStatus(
        clientId: Long,
        status: String
    ): List<FitnessAppointmentEntity>

    fun findByStaffIdAndStatus(
        trainerId: Long,
        status: String
    ): List<FitnessAppointmentEntity>

    @Query(
        """
        SELECT f
        FROM FitnessAppointmentEntity f
        WHERE (:clientId  IS NULL OR f.clientEntity.id = :clientId)
          AND (:trainerId IS NULL OR f.staff.id         = :trainerId)
          AND (:status    IS NULL OR f.status           = :status)
        """
    )
    fun search(
        @Param("clientId") clientId: Long?,
        @Param("trainerId") trainerId: Long?,
        @Param("status") status: String?
    ): List<FitnessAppointmentEntity>

    fun findByClientEntityIdAndStaffIdAndStatus(
        clientId: Long,
        trainerId: Long,
        status: String
    ): List<FitnessAppointmentEntity>
}