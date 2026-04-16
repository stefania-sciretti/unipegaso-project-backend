package com.clinica.doors.inbound.routes.mappers

import com.clinica.application.domain.Patient
import com.clinica.dto.PatientResponse
import java.time.LocalDateTime

fun Patient.toResponse() = PatientResponse(
    id = this.id,
    firstName = this.firstName,
    lastName = this.lastName,
    fiscalCode = this.fiscalCode,
    birthDate = this.birthDate,
    email = this.email,
    phone = this.phone,
    createdAt = LocalDateTime.now()
)