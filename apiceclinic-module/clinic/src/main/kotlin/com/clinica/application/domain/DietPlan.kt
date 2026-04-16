package com.clinica.application.domain

import java.time.LocalDateTime

data class DietPlan(
    val id: Long = 0,
    val client: Patient,
    val trainer: Staff,
    val title: String,
    val description: String? = null,
    val calories: Int? = null,
    val durationWeeks: Int? = null,
    val active: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)