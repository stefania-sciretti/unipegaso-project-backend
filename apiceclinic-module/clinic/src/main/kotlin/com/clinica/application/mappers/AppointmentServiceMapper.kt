package com.clinica.application.mappers

import com.clinic.model.AppointmentResponse
import com.clinica.application.domain.Appointment
import java.time.ZoneOffset

fun Appointment.toResponse(): AppointmentResponse =
    AppointmentResponse(
        id = this.id,
        patientId = this.patient.id,
        patientFullName = this.patient.fullName,
        specialistId = this.specialist.id,
        specialistFullName = this.specialist.fullName,
        specialistRole = this.specialist.role,
        scheduledAt = this.scheduledAt.atOffset(ZoneOffset.UTC),
        serviceType = this.serviceType,
        status = this.status.name,
        notes = this.notes,
        hasReport = false, // TODO Task-3: wire to ReportDao.existsByAppointmentId(this.id)
        areaId = this.areaId,
        areaName = this.areaName,
        price = this.price.toDouble(),
        createdAt = this.createdAt.atOffset(ZoneOffset.UTC)
    )
