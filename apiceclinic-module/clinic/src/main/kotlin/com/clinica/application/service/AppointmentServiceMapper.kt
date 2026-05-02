package com.clinica.application.service

import com.clinica.application.domain.Appointment
import com.clinic.model.AppointmentResponse
import java.time.ZoneOffset

fun Appointment.toResponse(): AppointmentResponse =
    AppointmentResponse(
        id = this.id,
        patientId = this.patient.id,
        patientFullName = this.patient.fullName,
        specialistId = this.specialist.id,
        specialistFullName = this.specialist.fullName,
        specialistSpecialization = this.specialist.role,
        scheduledAt = this.scheduledAt.atOffset(ZoneOffset.UTC),
        visitType = this.visitType,
        status = this.status.name,
        notes = this.notes,
        hasReport = false,
        createdAt = this.updatedAt.atOffset(ZoneOffset.UTC)
    )
