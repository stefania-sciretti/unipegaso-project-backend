package com.clinica.application.domain

import java.time.LocalDateTime

data class TrainingPlan(
    val id: Long = 0L,
    val patient: Patient,
    val specialist: Specialist,
    val title: String,
    val description: String? = null,
    val weeks: Int? = null,
    val sessionsPerWeek: Int? = null,
    val active: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
