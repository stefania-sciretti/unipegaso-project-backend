package com.clinica.application.domain

import java.time.LocalDateTime

data class Doctor(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val specialization: String,
    val email: String,
    val licenseNumber: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
