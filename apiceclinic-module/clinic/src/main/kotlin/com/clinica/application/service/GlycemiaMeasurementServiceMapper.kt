package com.clinica.application.service

import com.clinica.application.domain.GlycemiaMeasurement
import com.clinica.dto.GlycemiaMeasurementResponse

fun GlycemiaMeasurement.toResponse(): GlycemiaMeasurementResponse =
    GlycemiaMeasurementResponse(
        id = this.id,
        patientId = this.patient.id,
        patientFirstName = this.patient.firstName,
        patientLastName = this.patient.lastName,
        specialistId = this.specialist.id,
        specialistFirstName = this.specialist.firstName,
        specialistLastName = this.specialist.lastName,
        measuredAt = this.measuredAt,
        valueMgDl = this.valueMgDl,
        context = this.context.name,
        classification = this.classification.name,
        notes = this.notes,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
