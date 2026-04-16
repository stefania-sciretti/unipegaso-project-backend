package com.clinica.application.domain

import java.time.LocalDate
import java.time.LocalDateTime

data class Report(
    val id: Long,
    val appointment: Appointment,
    val issuedDate: LocalDate = LocalDate.now(),
    val diagnosis: String,
    val prescription: String? = null,
    val doctorNotes: String? = null,
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
