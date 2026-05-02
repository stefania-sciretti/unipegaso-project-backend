package com.clinica.dto

import jakarta.validation.constraints.*
import java.time.LocalDateTime

// ─── GlycemiaMeasurement ──────────────────────────────────────────────────────

data class GlycemiaMeasurementRequest(
    @field:NotNull(message = "Patient ID is required")
    val patientId: Long,

    @field:NotNull(message = "Specialist ID is required")
    val specialistId: Long,

    @field:NotNull(message = "Measurement date/time is required")
    val measuredAt: LocalDateTime,

    @field:NotNull(message = "Value in mg/dL is required")
    @field:Positive(message = "Value must be positive")
    @field:Min(value = 20, message = "Value must be at least 20 mg/dL")
    @field:Max(value = 600, message = "Value cannot exceed 600 mg/dL")
    val valueMgDl: Int,

    @field:NotBlank(message = "Context is required (A_DIGIUNO, POST_PASTO_1H or POST_PASTO_2H)")
    val context: String,

    val notes: String? = null
)
