package com.clinica.repository

import com.clinica.domain.AppointmentEntity
import com.clinica.application.domain.AppointmentStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface AppointmentRepository : JpaRepository<AppointmentEntity, Long> {
    fun findByPatientId(patientId: Long): List<AppointmentEntity>
    fun findByDoctorId(doctorId: Long): List<AppointmentEntity>
    fun findByStatus(status: AppointmentStatus): List<AppointmentEntity>
    fun findByPatientIdAndStatus(patientId: Long, status: AppointmentStatus): List<AppointmentEntity>
    fun findByDoctorIdAndScheduledAtBetween(
        doctorId: Long, from: LocalDateTime, to: LocalDateTime
    ): List<AppointmentEntity>
}
