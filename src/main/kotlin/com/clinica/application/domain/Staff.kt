package com.clinica.fitnessappointment.application.domain

import java.time.LocalDateTime

data class Staff(
    val id: Long = 0,
    var firstName: String,
    var lastName: String,
    var role: String,
    var bio: String? = null,
    var email: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
