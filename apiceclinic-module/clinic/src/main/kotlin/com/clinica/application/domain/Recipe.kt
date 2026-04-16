package com.clinica.application.domain

import java.time.LocalDateTime

data class Recipe(
    val id: Long,
    val title: String,
    val description: String?,
    val ingredients: String?,
    val instructions: String?,
    val calories: Int?,
    val category: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)