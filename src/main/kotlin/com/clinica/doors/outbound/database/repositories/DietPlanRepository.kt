package com.clinica.repository

import com.clinica.domain.DietPlanEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DietPlanRepository : JpaRepository<DietPlanEntity, Long> {
    fun findByClientId(clientId: Long): List<DietPlanEntity>
    fun findByClientIdAndActiveTrue(clientId: Long): List<DietPlanEntity>
    fun countByActiveTrue(): Long
}
