package com.clinica.application.domain

import java.time.LocalDate

data class ClientRequest(
    val firstName: String,
    val lastName: String,
    val fiscalCode: String,
    val birthDate: LocalDate?,
    val email: String,
    val phone: String?
)
