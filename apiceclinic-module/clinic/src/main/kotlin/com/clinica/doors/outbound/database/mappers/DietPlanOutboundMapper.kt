package com.clinica.doors.outbound.database.mappers

import com.clinica.application.domain.DietPlan
import com.clinica.doors.outbound.database.entities.DietPlanEntity
import com.clinica.doors.outbound.database.entities.PatientEntity
import com.clinica.doors.outbound.database.entities.SpecialistEntity

fun DietPlanEntity.toDomain(): DietPlan =
    DietPlan(
        id = this.id,
        patient = this.patientEntity.toDomain(),
        specialist = this.specialist.toDomain(),
        title = this.title,
        description = this.description,
        calories = this.calories,
        durationWeeks = this.durationWeeks,
        active = this.active,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )

fun DietPlan.toEntity(
    patientEntityProvider: () -> PatientEntity,
    specialistEntityProvider: () -> SpecialistEntity,
    existingEntity: DietPlanEntity? = null
): DietPlanEntity {
    val entity = existingEntity ?: DietPlanEntity(
        id = this.id,
        patientEntity = patientEntityProvider(),
        specialist = specialistEntityProvider(),
        title = this.title,
        description = this.description,
        calories = this.calories,
        durationWeeks = this.durationWeeks,
        active = this.active,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
    existingEntity?.let {
        it.patientEntity = patientEntityProvider()
        it.specialist = specialistEntityProvider()
        it.title = this.title
        it.description = this.description
        it.calories = this.calories
        it.durationWeeks = this.durationWeeks
        it.active = this.active
        it.updatedAt = this.updatedAt
    }
    return entity
}
