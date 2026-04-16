package com.clinica.application.domain

import java.time.LocalDateTime

data class FitnessAppointment(
    val id: Long,
    val client: Patient,
    val staff: Staff,
    val scheduledAt: LocalDateTime,
    val serviceType: String,
    val status: AppointmentStatus = AppointmentStatus.BOOKED,
    val notes: String? = null,
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
