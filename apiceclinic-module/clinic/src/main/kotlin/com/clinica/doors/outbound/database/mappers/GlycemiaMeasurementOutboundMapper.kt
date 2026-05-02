package com.clinica.doors.outbound.database.mappers

import com.clinica.application.domain.GlycemiaMeasurement
import com.clinica.doors.outbound.database.entities.GlycemiaMeasurementEntity
import com.clinica.doors.outbound.database.entities.PatientEntity
import com.clinica.doors.outbound.database.entities.SpecialistEntity

fun GlycemiaMeasurementEntity.toDomain(): GlycemiaMeasurement =
    GlycemiaMeasurement(
        id = this.id,
        patient = this.patientEntity.toDomain(),
        specialist = this.specialistEntity.toDomain(),
        measuredAt = this.measuredAt,
        valueMgDl = this.valueMgDl,
        context = this.context,
        notes = this.notes,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )

fun GlycemiaMeasurement.toEntity(
    patientEntityProvider: () -> PatientEntity,
    specialistEntityProvider: () -> SpecialistEntity,
    existingEntity: GlycemiaMeasurementEntity? = null
): GlycemiaMeasurementEntity {
    val entity = existingEntity ?: GlycemiaMeasurementEntity(
        id = this.id,
        patientEntity = patientEntityProvider(),
        specialistEntity = specialistEntityProvider(),
        measuredAt = this.measuredAt,
        valueMgDl = this.valueMgDl,
        context = this.context,
        notes = this.notes,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
    existingEntity?.let {
        it.patientEntity = patientEntityProvider()
        it.specialistEntity = specialistEntityProvider()
        it.measuredAt = this.measuredAt
        it.valueMgDl = this.valueMgDl
        it.context = this.context
        it.notes = this.notes
        it.updatedAt = this.updatedAt
    }
    return entity
}
