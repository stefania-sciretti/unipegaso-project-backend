package com.clinica.doors.outbound.database.mappers

import com.clinica.application.domain.TrainingPlan
import com.clinica.doors.outbound.database.entities.PatientEntity
import com.clinica.doors.outbound.database.entities.SpecialistEntity
import com.clinica.doors.outbound.database.entities.TrainingPlanEntity

fun TrainingPlanEntity.toDomain(): TrainingPlan =
    TrainingPlan(
        id = this.id,
        patient = this.patientEntity.toDomain(),
        specialist = this.specialist.toDomain(),
        title = this.title,
        description = this.description,
        weeks = this.weeks,
        sessionsPerWeek = this.sessionsPerWeek,
        active = this.active,
        createdAt = this.createdAt
    )

fun TrainingPlan.toEntity(
    patientEntityProvider: () -> PatientEntity,
    specialistEntityProvider: () -> SpecialistEntity,
    existingEntity: TrainingPlanEntity? = null
): TrainingPlanEntity {
    existingEntity?.let {
        it.patientEntity = patientEntityProvider()
        it.specialist = specialistEntityProvider()
        it.title = this.title
        it.description = this.description
        it.weeks = this.weeks
        it.sessionsPerWeek = this.sessionsPerWeek
        it.active = this.active
        return it
    }
    return TrainingPlanEntity(
        id = this.id,
        patientEntity = patientEntityProvider(),
        specialist = specialistEntityProvider(),
        title = this.title,
        description = this.description,
        weeks = this.weeks,
        sessionsPerWeek = this.sessionsPerWeek,
        active = this.active
    )
}
