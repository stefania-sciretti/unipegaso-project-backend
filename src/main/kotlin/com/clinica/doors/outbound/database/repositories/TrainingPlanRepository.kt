package com.clinica.repository

import com.clinica.domain.TrainingPlan
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TrainingPlanRepository : JpaRepository<TrainingPlan, Long> {
    fun findByClientId(clientId: Long): List<TrainingPlan>
    fun findByClientIdAndActiveTrue(clientId: Long): List<TrainingPlan>
    fun countByActiveTrue(): Long
}
