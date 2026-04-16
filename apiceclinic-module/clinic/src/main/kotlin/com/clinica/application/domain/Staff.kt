package com.clinica.application.domain

import java.time.LocalDateTime

data class Staff(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val role: String,
    val bio: String? = null,
    val email: String,
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
