package com.clinica.application.domain

import java.time.LocalDate
import java.time.LocalDateTime

data class Patient(
    val id: Long = 0L,
    val firstName: String,
    val lastName: String,
    val fiscalCode: String,
    val birthDate: LocalDate,
    val email: String,
    val phone: String? = null,
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
