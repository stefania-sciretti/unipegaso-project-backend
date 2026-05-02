package com.clinica.dto

import jakarta.validation.constraints.*
import java.time.LocalDate
import java.time.LocalDateTime

// ─── Patient ───────────────────────────────────────────────

data class PatientRequest(
    @field:NotBlank(message = "First name is required")
    val firstName: String,

    @field:NotBlank(message = "Last name is required")
    val lastName: String,

    @field:NotBlank(message = "Fiscal code is required")
    @field:Size(min = 16, max = 16, message = "Fiscal code must be 16 characters")
    @field:Pattern(
        regexp = "^[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]$",
        message = "Invalid Italian fiscal code format"
    )
    val fiscalCode: String,

    @field:NotNull(message = "Birth date is required")
    val birthDate: LocalDate,

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String,

    val phone: String? = null
)

data class PatientResponse(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val fiscalCode: String,
    val birthDate: LocalDate,
    val email: String,
    val phone: String?,
    val createdAt: LocalDateTime
)

// ─── Appointment ───────────────────────────────────────────

data class AppointmentRequest(
    @field:NotNull(message = "Patient ID is required")
    val patientId: Long,

    @field:NotNull(message = "specialist ID is required")
    val specialistId: Long,

    @field:NotNull(message = "Scheduled date/time is required")
    val scheduledAt: LocalDateTime,

    @field:NotBlank(message = "Visit type is required")
    val visitType: String,

    val notes: String? = null
)

data class AppointmentStatusRequest(
    @field:NotBlank(message = "Status is required")
    val status: String
)

data class AppointmentResponse(
    val id: Long,
    val patientId: Long,
    val patientFullName: String,
    val specialistId: Long,
    val specialistFullName: String,
    val specialistSpecialization: String,
    val scheduledAt: LocalDateTime,
    val visitType: String,
    val status: String,
    val notes: String?,
    val hasReport: Boolean,
    val createdAt: LocalDateTime
)

// ─── Report ────────────────────────────────────────────────

data class ReportRequest(
    @field:NotNull(message = "Appointment ID is required")
    val appointmentId: Long,

    @field:NotBlank(message = "Diagnosis is required")
    val diagnosis: String,

    val prescription: String? = null,
    val specialistNotes: String? = null
)

data class ReportResponse(
    val id: Long,
    val appointmentId: Long,
    val patientFullName: String,
    val specialistFullName: String,
    val visitType: String,
    val scheduledAt: LocalDateTime,
    val issuedDate: LocalDate,
    val diagnosis: String,
    val prescription: String?,
    val specialistNotes: String?,
    val createdAt: LocalDateTime
)

