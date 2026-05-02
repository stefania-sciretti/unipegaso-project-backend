package com.clinica.application.domain

import java.time.LocalDateTime

data class Appointment(
    val id: Long,
    val patient: Patient,
    val specialist: Specialist,
    val scheduledAt: LocalDateTime,
    val visitType: String,
    val status: AppointmentStatusEnum = AppointmentStatusEnum.BOOKED,
    val notes: String? = null,
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
