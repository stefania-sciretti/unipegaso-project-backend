package com.clinica.doors.outbound.database.mappers

import com.clinica.application.domain.Specialist
import com.clinica.doors.outbound.database.entities.SpecialistEntity

fun SpecialistEntity.toDomain(): Specialist =
    Specialist(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        role = this.role,
        bio = this.bio,
        email = this.email,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
