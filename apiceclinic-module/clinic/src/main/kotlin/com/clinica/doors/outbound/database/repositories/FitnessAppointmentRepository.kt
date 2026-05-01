package com.clinica.doors.outbound.database.repositories

import com.clinica.doors.outbound.database.entities.FitnessAppointmentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface FitnessAppointmentRepository : JpaRepository<FitnessAppointmentEntity, Long> {

    fun findByPatientEntityId(patientId: Long): List<FitnessAppointmentEntity>

    fun findBySpecialistId(specialistId: Long): List<FitnessAppointmentEntity>

    fun findByStatus(status: String): List<FitnessAppointmentEntity>

    fun findByPatientEntityIdAndSpecialistId(
        patientId: Long,
        specialistId: Long
    ): List<FitnessAppointmentEntity>

    fun findByPatientEntityIdAndStatus(
        patientId: Long,
        status: String
    ): List<FitnessAppointmentEntity>

    fun findBySpecialistIdAndStatus(
        specialistId: Long,
        status: String
    ): List<FitnessAppointmentEntity>

    @Query(
        """
        SELECT f
        FROM FitnessAppointmentEntity f
        WHERE (:patientId   IS NULL OR f.patientEntity.id = :patientId)
          AND (:specialistId IS NULL OR f.specialist.id   = :specialistId)
          AND (:status      IS NULL OR f.status           = :status)
        """
    )
    fun search(
        @Param("patientId") patientId: Long?,
        @Param("specialistId") specialistId: Long?,
        @Param("status") status: String?
    ): List<FitnessAppointmentEntity>

    fun findByPatientEntityIdAndSpecialistIdAndStatus(
        patientId: Long,
        specialistId: Long,
        status: String
    ): List<FitnessAppointmentEntity>
}