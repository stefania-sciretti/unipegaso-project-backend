package com.clinica.doors.outbound.database.mappers

import com.clinica.application.domain.Report
import com.clinica.doors.outbound.database.entities.ReportEntity

fun ReportEntity.toDomain(): Report =
    Report(
        id = id,
        appointment = appointmentEntity.toDomain(),
        issuedDate = issuedDate,
        diagnosis = diagnosis,
        prescription = prescription,
        specialistNotes = specialistNotes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
