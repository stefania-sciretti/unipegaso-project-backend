package com.clinica.application.service

import com.clinica.dto.DietPlanRequest
import com.clinica.dto.DietPlanResponse

interface DietPlanServicePort {
    fun findAll(clientId: Long?): List<DietPlanResponse>
    fun findById(id: Long): DietPlanResponse
    fun create(request: DietPlanRequest): DietPlanResponse
    fun update(id: Long, request: DietPlanRequest): DietPlanResponse
    fun delete(id: Long)
}
