package com.clinica.application.domain

import java.time.LocalDateTime

data class Appointment(
    val id: Long,
    val patient: Patient,
    val doctor: Doctor,
    val scheduledAt: LocalDateTime,
    val visitType: String,
    val status: AppointmentStatus = AppointmentStatus.BOOKED,
    val notes: String? = null,
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val report: Report? = null
)
