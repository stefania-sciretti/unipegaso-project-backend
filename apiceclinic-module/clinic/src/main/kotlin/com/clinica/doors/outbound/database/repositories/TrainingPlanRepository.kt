package com.clinica.doors.outbound.database.repositories

import com.clinica.doors.outbound.database.entities.TrainingPlanEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TrainingPlanRepository : JpaRepository<TrainingPlanEntity, Long> {
    fun findByClientEntityId(clientId: Long): List<TrainingPlanEntity>
    fun countByActiveTrue(): Long
}
