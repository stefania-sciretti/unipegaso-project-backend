package com.clinica.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
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

// ─── Doctor ────────────────────────────────────────────────

data class DoctorRequest(
    @field:NotBlank(message = "First name is required")
    val firstName: String,

    @field:NotBlank(message = "Last name is required")
    val lastName: String,

    @field:NotBlank(message = "Specialization is required")
    val specialization: String,

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String,

    @field:NotBlank(message = "License number is required")
    val licenseNumber: String
)

data class DoctorResponse(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val specialization: String,
    val email: String,
    val licenseNumber: String,
    val createdAt: LocalDateTime
)

// ─── Appointment ───────────────────────────────────────────

data class AppointmentRequest(
    @field:NotNull(message = "Patient ID is required")
    val patientId: Long,

    @field:NotNull(message = "Doctor ID is required")
    val doctorId: Long,

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
    val doctorId: Long,
    val doctorFullName: String,
    val doctorSpecialization: String,
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
    val doctorNotes: String? = null
)

data class ReportResponse(
    val id: Long,
    val appointmentId: Long,
    val patientFullName: String,
    val doctorFullName: String,
    val visitType: String,
    val scheduledAt: LocalDateTime,
    val issuedDate: LocalDate,
    val diagnosis: String,
    val prescription: String?,
    val doctorNotes: String?,
    val createdAt: LocalDateTime
)

