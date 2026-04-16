package com.clinica.fitnessappointment.application.domain

import java.time.LocalDate
import java.time.LocalDateTime

data class Patient(
    val id: Long = 0,
    var firstName: String,
    var lastName: String,
    var fiscalCode: String,
    var birthDate: LocalDate,
    var email: String,
    var phone: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
