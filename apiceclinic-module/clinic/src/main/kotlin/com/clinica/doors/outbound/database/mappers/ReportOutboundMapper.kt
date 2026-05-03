package com.clinica.doors.outbound.database.mappers

import com.clinica.application.domain.Appointment
import com.clinica.application.domain.Report
import com.clinica.doors.outbound.database.entities.ReportEntity

fun ReportEntity.toDomain(): Report =
    Report(
        id = id,
        appointment = Appointment(
            id = fitnessAppointmentEntity.id ?: 0L,
            patient = fitnessAppointmentEntity.patientEntity.toDomain(),
            specialist = fitnessAppointmentEntity.specialist.toDomain(),
            scheduledAt = fitnessAppointmentEntity.scheduledAt,
            visitType = fitnessAppointmentEntity.serviceType,
            status = fitnessAppointmentEntity.status,
            notes = fitnessAppointmentEntity.notes,
            updatedAt = fitnessAppointmentEntity.updatedAt
        ),
        issuedDate = issuedDate,
        diagnosis = diagnosis,
        prescription = prescription,
        specialistNotes = specialistNotes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
