package com.clinica.application.mappers

import com.clinic.model.FitnessAppointmentResponse
import com.clinica.application.domain.Appointment
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

fun FitnessAppointment.toAppointment(): Appointment =
    Appointment(
        id = id,
        patient = patient,
        specialist = specialist,
        scheduledAt = scheduledAt,
        serviceType = serviceType,
        status = status,
        notes = notes,
        updatedAt = updatedAt
    )
