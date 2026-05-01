package com.clinica.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.time.LocalDateTime

// ─── GlycemiaMeasurement ─────────────────────────────────────────────────────

data class GlycemiaMeasurementRequest(
    @field:NotNull(message = "Patient ID is required")
    val patientId: Long,
    @field:NotNull(message = "Specialist ID is required")
    val specialistId: Long,
    @field:NotNull(message = "Measurement date/time is required")
    val measuredAt: LocalDateTime,
    @field:NotNull(message = "Glycemia value is required")
    @field:Positive(message = "Value must be positive")
    val valueMgDl: BigDecimal,
    /** FASTING | POST_MEAL_1H | POST_MEAL_2H | RANDOM */
    val context: String = "FASTING",
    val notes: String? = null
)

data class GlycemiaMeasurementResponse(
    val id: Long,
    val patientId: Long,
    val patientFullName: String,
    val specialistId: Long,
    val specialistFullName: String,
    val measuredAt: LocalDateTime,
    val valueMgDl: BigDecimal,
    val context: String,
    /** Classificazione automatica: NORMALE / ATTENZIONE / ELEVATA */
    val classification: String,
    val notes: String?,
    val createdAt: LocalDateTime
)
