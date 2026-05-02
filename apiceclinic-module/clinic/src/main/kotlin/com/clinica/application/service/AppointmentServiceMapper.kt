package com.clinica.application.service

import com.clinica.application.domain.Appointment
import com.clinica.dto.AppointmentResponse

fun Appointment.toResponse(): AppointmentResponse =
    AppointmentResponse(
        id = this.id,
        patientId = this.patient.id,
        patientFullName = this.patient.fullName,
        specialistId = this.specialist.id,
        specialistFullName = this.specialist.fullName,
        specialistSpecialization = this.specialist.role,
        scheduledAt = this.scheduledAt,
        visitType = this.visitType,
        status = this.status.name,
        notes = this.notes,
        hasReport = false,
        createdAt = this.updatedAt
    )
