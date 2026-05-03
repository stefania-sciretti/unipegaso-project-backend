package com.clinica.application.domain

import java.math.BigDecimal

data class ClinicService(
    val id: Long = 0L,
    val service: String,
    val price: BigDecimal,
    val specialistId: Long,
    val areaId: Long? = null,
    val areaName: String? = null
)
