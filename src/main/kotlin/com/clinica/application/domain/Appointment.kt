package com.clinica.fitnessappointment.application.domain

import java.time.LocalDateTime

data class Appointment(
    val id: Long = 0,
    var patient: Patient,
    var doctor: Doctor,
    var scheduledAt: LocalDateTime,
    var visitType: String,
    var status: AppointmentStatus = AppointmentStatus.BOOKED,
    var notes: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    var report: Report? = null
)
