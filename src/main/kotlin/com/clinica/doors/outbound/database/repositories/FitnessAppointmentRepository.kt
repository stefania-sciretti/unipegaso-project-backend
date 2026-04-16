package com.clinica.fitnessappointment.doors.outbound.database

import com.clinica.fitnessappointment.application.domain.FitnessAppointment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FitnessAppointmentRepository : JpaRepository<FitnessAppointment, Long> {
    fun findByClientId(clientId: Long): List<FitnessAppointment>
    fun findByStaffId(staffId: Long): List<FitnessAppointment>
    fun findByStatus(status: AppointmentStatus): List<FitnessAppointment>
}
