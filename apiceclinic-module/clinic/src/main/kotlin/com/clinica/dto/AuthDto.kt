package com.clinica.dto

import java.time.LocalDate

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val accessToken: String,
    val tokenType: String = "Bearer",
    val username: String,
    val role: String = "ROLE_USER"
)

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val fiscalCode: String,
    val birthDate: LocalDate,
    val email: String,
    val phone: String? = null,
    val username: String,
    val password: String
)

data class RegisterResponse(
    val message: String,
    val success: Boolean,
    val username: String,
    val email: String,
    val patientId: Long
)
