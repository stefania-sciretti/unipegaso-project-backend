package com.clinica.application.mappers

import com.clinic.model.ServiceResponse
import com.clinica.application.domain.ClinicService

fun ClinicService.toResponse(): ServiceResponse =
    ServiceResponse(
        id = id,
        service = service,
        price = price.toDouble(),
        specialistId = specialistId,
        areaId = areaId,
        areaName = areaName
    )
