package com.clinica.doors.outbound.database.repositories

import com.clinica.application.domain.AppointmentStatusEnum
import com.clinica.doors.outbound.database.entities.FitnessAppointmentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface FitnessAppointmentRepository : JpaRepository<FitnessAppointmentEntity, Long> {

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
        @Param("status") status: AppointmentStatusEnum?
    ): List<FitnessAppointmentEntity>


}