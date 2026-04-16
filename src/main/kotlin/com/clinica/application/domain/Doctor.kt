package com.clinica.fitnessappointment.application.domain

import java.time.LocalDateTime

data class Doctor(
    val id: Long = 0,
    var firstName: String,
    var lastName: String,
    var specialization: String,
    var email: String,
    var licenseNumber: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
