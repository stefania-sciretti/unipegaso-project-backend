package com.clinica.doors.outbound.database.mappers

import com.clinica.application.domain.Specialist
import com.clinica.doors.outbound.database.entities.AreaEntity
import com.clinica.doors.outbound.database.entities.SpecialistEntity

fun SpecialistEntity.toDomain(): Specialist =
    Specialist(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        role = this.role,
        bio = this.bio,
        email = this.email,
        areaId = this.area?.id,
        areaName = this.area?.name,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )

fun Specialist.toEntity(areaEntity: AreaEntity? = null): SpecialistEntity =
    SpecialistEntity(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        role = this.role,
        bio = this.bio,
        email = this.email,
        area = areaEntity,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
