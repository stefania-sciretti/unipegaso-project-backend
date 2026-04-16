package com.clinica.doors.outbound.database.mappers

import com.clinica.application.domain.Staff
import com.clinica.doors.outbound.database.entities.StaffEntity

fun StaffEntity.toDomain(): Staff =
    Staff(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        role = this.role,
        bio = this.bio,
        email = this.email,
        updatedAt = this.updatedAt
    )
