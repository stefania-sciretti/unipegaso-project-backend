package com.clinica.application.mappers

import com.clinic.model.TrainingPlanResponse
import com.clinica.application.domain.TrainingPlan
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
