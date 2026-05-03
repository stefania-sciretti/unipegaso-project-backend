package com.clinica.doors.outbound.database.mappers

import com.clinica.application.domain.ClinicService
import com.clinica.doors.outbound.database.entities.AreaEntity
import com.clinica.doors.outbound.database.entities.ServiceEntity
import com.clinica.doors.outbound.database.entities.SpecialistEntity

fun ServiceEntity.toDomain(): ClinicService =
    ClinicService(
        id = this.id,
        service = this.service,
        price = this.price,
        specialistId = this.specialist.id,
        areaId = this.area?.id,
        areaName = this.area?.name
    )

fun ClinicService.toEntity(specialistEntity: SpecialistEntity, areaEntity: AreaEntity? = null): ServiceEntity =
    ServiceEntity(
        id = this.id,
        service = this.service,
        price = this.price,
        specialist = specialistEntity,
        area = areaEntity
    )
