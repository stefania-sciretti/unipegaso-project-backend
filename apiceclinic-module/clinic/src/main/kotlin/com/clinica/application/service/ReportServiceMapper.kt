package com.clinica.application.service

import com.clinica.doors.outbound.database.entities.ReportEntity
import com.clinic.model.ReportResponse
import java.time.ZoneOffset

fun ReportEntity.toResponse(): ReportResponse =
    ReportResponse(
        id = id,
        appointmentId = fitnessAppointmentEntity.id ?: 0L,
        patientFullName = "${fitnessAppointmentEntity.patientEntity.firstName} ${fitnessAppointmentEntity.patientEntity.lastName}",
        specialistFullName = "${fitnessAppointmentEntity.specialist.firstName} ${fitnessAppointmentEntity.specialist.lastName}",
        visitType = fitnessAppointmentEntity.serviceType,
        scheduledAt = fitnessAppointmentEntity.scheduledAt.atOffset(ZoneOffset.UTC),
        issuedDate = issuedDate,
        diagnosis = diagnosis,
        prescription = prescription,
        specialistNotes = specialistNotes,
        createdAt = createdAt.atOffset(ZoneOffset.UTC)
    )
