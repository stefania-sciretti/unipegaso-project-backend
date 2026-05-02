package com.clinica.application.service

import com.clinica.application.domain.Specialist
import com.clinica.dto.SpecialistResponse

fun Specialist.toResponse(): SpecialistResponse =
    SpecialistResponse(
        id = id,
        firstName = firstName,
        lastName = lastName,
        role = role,
        bio = bio,
        email = email,
        createdAt = createdAt
    )
