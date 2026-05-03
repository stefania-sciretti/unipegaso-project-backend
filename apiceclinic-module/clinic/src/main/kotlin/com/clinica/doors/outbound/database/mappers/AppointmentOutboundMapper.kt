package com.clinica.doors.outbound.database.mappers

import com.clinica.application.domain.Appointment
import com.clinica.application.domain.AppointmentStatusEnum
import com.clinica.doors.outbound.database.entities.AppointmentEntity
import com.clinica.doors.outbound.database.entities.AreaEntity
import com.clinica.doors.outbound.database.entities.PatientEntity
import com.clinica.doors.outbound.database.entities.SpecialistEntity

fun AppointmentEntity.toDomain(): Appointment =
    Appointment(
        id = this.id,
        patient = this.patientEntity.toDomain(),
        specialist = this.specialistEntity.toDomain(),
        scheduledAt = this.scheduledAt,
        serviceType = this.serviceType,
        status = AppointmentStatusEnum.valueOf(this.status),
        notes = this.notes,
        price = this.price,
        createdAt = this.createdAt,
        areaId = this.area?.id,
        areaName = this.area?.name,
        updatedAt = this.updatedAt
    )

fun Appointment.toEntity(
    patientEntityProvider: () -> PatientEntity,
    specialistEntityProvider: () -> SpecialistEntity,
    areaEntityProvider: () -> AreaEntity?,
    existingEntity: AppointmentEntity? = null
): AppointmentEntity =
    existingEntity?.apply {
        patientEntity = patientEntityProvider()
        specialistEntity = specialistEntityProvider()
        scheduledAt = this@toEntity.scheduledAt
        serviceType = this@toEntity.serviceType
        status = this@toEntity.status.name
        notes = this@toEntity.notes
        price = this@toEntity.price
        area = areaEntityProvider()
        updatedAt = this@toEntity.updatedAt
    } ?: AppointmentEntity(
        id = id,
        patientEntity = patientEntityProvider(),
        specialistEntity = specialistEntityProvider(),
        scheduledAt = scheduledAt,
        serviceType = serviceType,
        status = status.name,
        notes = notes,
        price = price,
        createdAt = createdAt,
        area = areaEntityProvider(),
        updatedAt = updatedAt
    )
