package com.clinica.service.api

import com.clinica.dto.TrainingPlanRequest
import com.clinica.dto.TrainingPlanResponse

/**
 * Contract for training plan management operations.
 */
interface TrainingPlanServicePort {
    fun findAll(clientId: Long?): List<TrainingPlanResponse>
    fun findById(id: Long): TrainingPlanResponse
    fun create(request: TrainingPlanRequest): TrainingPlanResponse
    fun update(id: Long, request: TrainingPlanRequest): TrainingPlanResponse
    fun delete(id: Long)
}
