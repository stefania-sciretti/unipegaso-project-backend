package com.clinica.doors.outbound.database.mappers

import com.clinica.application.domain.DietPlan
import com.clinica.doors.outbound.database.entities.DietPlanEntity
import com.clinica.doors.outbound.database.entities.PatientEntity
import com.clinica.doors.outbound.database.entities.StaffEntity

fun DietPlanEntity.toDomain(): DietPlan =
    DietPlan(
        id = this.id,
        client = this.clientEntity.toDomain(),
        trainer = this.staff.toDomain(),
        title = this.title,
        description = this.description,
        calories = this.calories,
        durationWeeks = this.durationWeeks,
        active = this.active,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )

fun DietPlan.toEntity(
    clientEntityProvider: () -> PatientEntity,
    staffEntityProvider: () -> StaffEntity,
    existingEntity: DietPlanEntity? = null
): DietPlanEntity {
    val entity = existingEntity ?: DietPlanEntity(
        id = this.id,
        clientEntity = clientEntityProvider(),
        staff = staffEntityProvider(),
        title = this.title,
        description = this.description,
        calories = this.calories,
        durationWeeks = this.durationWeeks,
        active = this.active,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
    existingEntity?.let {
        it.clientEntity = clientEntityProvider()
        it.staff = staffEntityProvider()
        it.title = this.title
        it.description = this.description
        it.calories = this.calories
        it.durationWeeks = this.durationWeeks
        it.active = this.active
        it.updatedAt = this.updatedAt
    }
    return entity
}
