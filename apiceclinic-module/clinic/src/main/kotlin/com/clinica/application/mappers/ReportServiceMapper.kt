package com.clinica.application.mappers

import com.clinic.model.ReportResponse
import com.clinica.application.domain.Report
import java.time.ZoneOffset

fun Report.toResponse(): ReportResponse =
    ReportResponse(
        id = id,
        appointmentId = appointment.id,
        patientFullName = appointment.patient.fullName,
        specialistFullName = appointment.specialist.fullName,
        visitType = appointment.visitType,
        scheduledAt = appointment.scheduledAt.atOffset(ZoneOffset.UTC),
        issuedDate = issuedDate,
        diagnosis = diagnosis,
        prescription = prescription,
        specialistNotes = specialistNotes,
        createdAt = createdAt.atOffset(ZoneOffset.UTC)
    )
