package com.clinica.application.mappers

import com.clinic.model.AreaResponse
import com.clinic.model.SpecialistResponse
import com.clinica.application.domain.Specialist
import java.time.ZoneOffset

fun Specialist.toResponse(): SpecialistResponse =
    SpecialistResponse(
        id = id,
        firstName = firstName,
        lastName = lastName,
        role = role,
        bio = bio,
        email = email,
        area = if (areaId != null && areaName != null) AreaResponse(id = areaId, name = areaName) else null,
        createdAt = createdAt.atOffset(ZoneOffset.UTC)
    )
