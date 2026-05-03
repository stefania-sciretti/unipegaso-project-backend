package com.clinica.application.domain

import java.math.BigDecimal
import java.time.LocalDateTime

data class Appointment(
    val id: Long,
    val patient: Patient,
    val specialist: Specialist,
    val scheduledAt: LocalDateTime,
    val serviceType: String,
    val status: AppointmentStatusEnum = AppointmentStatusEnum.BOOKED,
    val notes: String? = null,
    val price: BigDecimal = BigDecimal.ZERO,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val areaId: Long? = null,
    val areaName: String? = null,
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val hasReport: Boolean = false
)
