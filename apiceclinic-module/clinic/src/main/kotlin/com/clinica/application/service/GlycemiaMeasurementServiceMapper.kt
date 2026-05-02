package com.clinica.application.service

import com.clinica.application.domain.GlycemiaMeasurement
import com.clinic.model.GlycemiaMeasurementResponse
import java.time.ZoneOffset

fun GlycemiaMeasurement.toResponse(): GlycemiaMeasurementResponse =
    GlycemiaMeasurementResponse(
        id = this.id,
        patientId = this.patient.id,
        patientFirstName = this.patient.firstName,
        patientLastName = this.patient.lastName,
        specialistId = this.specialist.id,
        specialistFirstName = this.specialist.firstName,
        specialistLastName = this.specialist.lastName,
        measuredAt = this.measuredAt.atOffset(ZoneOffset.UTC),
        valueMgDl = this.valueMgDl,
        context = GlycemiaMeasurementResponse.Context.valueOf(this.context.name),
        classification = GlycemiaMeasurementResponse.Classification.valueOf(this.classification.name),
        notes = this.notes,
        createdAt = this.createdAt.atOffset(ZoneOffset.UTC),
        updatedAt = this.updatedAt.atOffset(ZoneOffset.UTC)
    )
