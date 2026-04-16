package com.clinica.doors.outbound.database.mappers

import com.clinica.application.domain.Patient
import com.clinica.doors.outbound.database.entities.PatientEntity

fun PatientEntity.toDomain(): Patient =
    Patient(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        fiscalCode = this.fiscalCode,
        birthDate = this.birthDate,
        email = this.email,
        phone = this.phone,
        updatedAt = this.updatedAt
    )

fun Patient.toEntity(): PatientEntity =
    PatientEntity(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        fiscalCode = this.fiscalCode,
        birthDate = this.birthDate,
        email = this.email,
        phone = this.phone,
        updatedAt = this.updatedAt
    )