package com.clinica.fitnessappointment.application.domain

import java.time.LocalDate
import java.time.LocalDateTime

data class Report(
    val id: Long = 0,
    var appointment: Appointment,
    var issuedDate: LocalDate = LocalDate.now(),
    var diagnosis: String,
    var prescription: String? = null,
    var doctorNotes: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
