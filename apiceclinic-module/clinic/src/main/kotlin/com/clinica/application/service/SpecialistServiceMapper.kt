package com.clinica.application.service

import com.clinica.application.domain.Specialist
import com.clinic.model.SpecialistResponse
import java.time.ZoneOffset

fun Specialist.toResponse(): SpecialistResponse =
    SpecialistResponse(
        id = id,
        firstName = firstName,
        lastName = lastName,
        role = role,
        bio = bio,
        email = email,
        createdAt = createdAt.atOffset(ZoneOffset.UTC)
    )
