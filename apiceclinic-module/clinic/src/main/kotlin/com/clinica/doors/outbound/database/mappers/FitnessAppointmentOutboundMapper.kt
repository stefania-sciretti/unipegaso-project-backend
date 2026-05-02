package com.clinica.doors.outbound.database.mappers

import com.clinica.application.domain.FitnessAppointment
import com.clinica.doors.outbound.database.entities.FitnessAppointmentEntity

fun FitnessAppointmentEntity.toDomain(): FitnessAppointment =
    FitnessAppointment(
        id = id ?: 0L,
        patient = patientEntity.toDomain(),
        specialist = specialist.toDomain(),
        scheduledAt = scheduledAt,
        serviceType = serviceType,
        status = status,
        notes = notes,
        updatedAt = updatedAt
    )
