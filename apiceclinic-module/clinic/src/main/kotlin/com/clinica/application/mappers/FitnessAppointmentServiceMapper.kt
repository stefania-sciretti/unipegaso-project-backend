package com.clinica.application.mappers

import com.clinic.model.FitnessAppointmentResponse
import com.clinica.application.domain.FitnessAppointment
import java.time.ZoneOffset

fun FitnessAppointment.toResponse(): FitnessAppointmentResponse =
    FitnessAppointmentResponse(
        id = id,
        patientId = patient.id,
        patientFullName = patient.fullName,
        specialistId = specialist.id,
        specialistFullName = specialist.fullName,
        specialistRole = specialist.role,
        scheduledAt = scheduledAt.atOffset(ZoneOffset.UTC),
        serviceType = serviceType,
        status = status.name,
        notes = notes,
        createdAt = updatedAt.atOffset(ZoneOffset.UTC)
    )
