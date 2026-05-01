package com.clinica.application.domain

import java.time.LocalDateTime

data class FitnessAppointment(
    val id: Long,
    val patient: Patient,
    val specialist: Specialist,
    val scheduledAt: LocalDateTime,
    val serviceType: String,
    val status: AppointmentStatus = AppointmentStatus.BOOKED,
    val notes: String? = null,
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
