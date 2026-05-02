package com.clinica.doors.outbound.database.mappers

import com.clinica.application.domain.Appointment
import com.clinica.application.domain.AppointmentStatusEnum
import com.clinica.doors.outbound.database.entities.AppointmentEntity
import com.clinica.doors.outbound.database.entities.PatientEntity
import com.clinica.doors.outbound.database.entities.SpecialistEntity

fun AppointmentEntity.toDomain(): Appointment =
    Appointment(
        id = this.id,
        patient = this.patientEntity.toDomain(),
        specialist = this.specialistEntity.toDomain(),
        scheduledAt = this.scheduledAt,
        visitType = this.visitType,
        status = AppointmentStatusEnum.valueOf(this.status),
        notes = this.notes,
        updatedAt = this.updatedAt
    )

fun Appointment.toEntity(
    patientEntityProvider: () -> PatientEntity,
    specialistEntityProvider: () -> SpecialistEntity,
    existingEntity: AppointmentEntity? = null
): AppointmentEntity {
    val entity = existingEntity ?: AppointmentEntity(
        id = this.id,
        patientEntity = patientEntityProvider(),
        specialistEntity = specialistEntityProvider(),
        scheduledAt = this.scheduledAt,
        visitType = this.visitType,
        status = this.status.name,
        notes = this.notes,
        updatedAt = this.updatedAt
    )
    existingEntity?.let {
        it.patientEntity = patientEntityProvider()
        it.specialistEntity = specialistEntityProvider()
        it.scheduledAt = this.scheduledAt
        it.visitType = this.visitType
        it.status = this.status.name
        it.notes = this.notes
        it.updatedAt = this.updatedAt
    }
    return entity
}


