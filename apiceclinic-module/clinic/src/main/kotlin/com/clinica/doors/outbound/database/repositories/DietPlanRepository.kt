package com.clinica.doors.outbound.database.repositories

import com.clinica.doors.outbound.database.entities.DietPlanEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DietPlanRepository : JpaRepository<DietPlanEntity, Long> {

    fun findByClientEntityId(clientId: Long): List<DietPlanEntity>

    fun findByClientEntityIdAndActiveTrue(clientId: Long): List<DietPlanEntity>

    fun findByStaffId(trainerId: Long): List<DietPlanEntity>

    fun findByStaffIdAndActiveTrue(trainerId: Long): List<DietPlanEntity>

    fun countByActiveTrue(): Long
}