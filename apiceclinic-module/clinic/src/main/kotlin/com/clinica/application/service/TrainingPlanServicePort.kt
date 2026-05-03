package com.clinica.application.service

import com.clinic.model.TrainingPlanRequest
import com.clinica.application.domain.TrainingPlan

interface TrainingPlanServicePort {
    fun findAll(patientId: Long?): List<TrainingPlan>
    fun findById(id: Long): TrainingPlan
    fun create(request: TrainingPlanRequest): TrainingPlan
    fun update(id: Long, request: TrainingPlanRequest): TrainingPlan
    fun delete(id: Long)
}
