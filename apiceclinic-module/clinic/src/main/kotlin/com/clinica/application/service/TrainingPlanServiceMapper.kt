package com.clinica.application.service

import com.clinica.application.domain.TrainingPlan
import com.clinic.model.TrainingPlanResponse
import java.time.ZoneOffset

fun TrainingPlan.toResponse(): TrainingPlanResponse =
    TrainingPlanResponse(
        id = id,
        patientId = patient.id,
        patientFullName = patient.fullName,
        specialistId = specialist.id,
        specialistFullName = specialist.fullName,
        title = title,
        description = description,
        weeks = weeks,
        sessionsPerWeek = sessionsPerWeek,
        active = active,
        createdAt = createdAt.atOffset(ZoneOffset.UTC)
    )
