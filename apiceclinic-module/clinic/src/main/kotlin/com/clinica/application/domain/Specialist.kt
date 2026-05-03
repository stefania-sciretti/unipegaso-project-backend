package com.clinica.application.domain

import java.time.LocalDateTime

data class Specialist(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val role: String,
    val bio: String? = null,
    val email: String,
    val areaId: Long? = null,
    val areaName: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    val fullName: String get() = "$firstName $lastName"
}
