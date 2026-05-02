package com.clinica.application.mappers

import com.clinic.model.DietPlanResponse
import com.clinica.application.domain.DietPlan
import java.time.ZoneOffset

fun DietPlan.toResponse(): DietPlanResponse =
    DietPlanResponse(
        id = this.id,
        patientId = this.patient.id,
        specialistId = this.specialist.id,
        patientFirstName = this.patient.firstName,
        patientLastName = this.patient.lastName,
        specialistFirstName = this.specialist.firstName,
        specialistLastName = this.specialist.lastName,
        title = this.title,
        description = this.description,
        calories = this.calories,
        durationWeeks = this.durationWeeks,
        active = this.active,
        createdAt = this.createdAt.atOffset(ZoneOffset.UTC),
        updatedAt = this.updatedAt.atOffset(ZoneOffset.UTC)
    )
