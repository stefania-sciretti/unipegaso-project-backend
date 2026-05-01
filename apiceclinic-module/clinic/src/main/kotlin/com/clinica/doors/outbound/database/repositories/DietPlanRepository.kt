package com.clinica.doors.outbound.database.repositories

import com.clinica.doors.outbound.database.entities.DietPlanEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DietPlanRepository : JpaRepository<DietPlanEntity, Long> {

    fun findByPatientEntityId(patientId: Long): List<DietPlanEntity>

    fun findByPatientEntityIdAndActiveTrue(patientId: Long): List<DietPlanEntity>

    fun findBySpecialistId(specialistId: Long): List<DietPlanEntity>

    fun findBySpecialistIdAndActiveTrue(specialistId: Long): List<DietPlanEntity>

    fun countByActiveTrue(): Long
}