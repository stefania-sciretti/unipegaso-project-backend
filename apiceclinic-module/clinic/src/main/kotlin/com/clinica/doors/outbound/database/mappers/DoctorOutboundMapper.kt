package com.clinica.doors.outbound.database.mappers

import com.clinica.application.domain.Doctor
import com.clinica.doors.outbound.database.entities.DoctorEntity

fun DoctorEntity.toDomain(): Doctor =
    Doctor(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        specialization = this.specialization,
        email = this.email,
        licenseNumber = this.licenseNumber,
        createdAt = this.createdAt
    )

fun Doctor.toEntity(): DoctorEntity =
    DoctorEntity(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        specialization = this.specialization,
        email = this.email,
        licenseNumber = this.licenseNumber
    )
