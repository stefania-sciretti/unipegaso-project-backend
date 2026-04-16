package com.clinica.doors.outbound.database.repositories

import com.clinica.doors.outbound.database.entities.AppointmentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface AppointmentRepository : JpaRepository<AppointmentEntity, Long> {

    fun findByPatientEntityId(patientId: Long): List<AppointmentEntity>

    fun findByDoctorId(doctorId: Long): List<AppointmentEntity>

    fun findByStatus(status: String): List<AppointmentEntity>

    fun countByStatus(status: String): Long

    fun findByPatientEntityIdAndDoctorId(patientId: Long, doctorId: Long): List<AppointmentEntity>

    fun findByPatientEntityIdAndStatus(patientId: Long, status: String): List<AppointmentEntity>

    fun findByDoctorIdAndStatus(doctorId: Long, status: String): List<AppointmentEntity>

    @Query(
        """
        SELECT a
        FROM AppointmentEntity a
        WHERE (:patientId IS NULL OR a.patientEntity.id = :patientId)
          AND (:doctorId  IS NULL OR a.doctor.id = :doctorId)
          AND (:status    IS NULL OR a.status = :status)
        """
    )
    fun search(
        @Param("patientId") patientId: Long?,
        @Param("doctorId") doctorId: Long?,
        @Param("status") status: String?
    ): List<AppointmentEntity>
    fun findByPatientEntityIdAndDoctorIdAndStatus(
        patientId: Long,
        doctorId: Long,
        status: String
    ): List<AppointmentEntity>
}