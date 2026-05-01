package com.clinica.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

// ─── Specialist ─────────────────────────────────────────────────────────────────

data class SpecialistResponse(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val role: String,
    val bio: String?,
    val email: String,
    val createdAt: LocalDateTime
)

data class SpecialistRequest(
    @field:NotBlank(message = "First name is required")
    val firstName: String,

    @field:NotBlank(message = "Last name is required")
    val lastName: String,

    @field:NotBlank(message = "Role is required")
    val role: String,

    val bio: String? = null,

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String
)

// ─── FitnessAppointment ───────────────────────────────────────────────────────

data class FitnessAppointmentRequest(
    @field:NotNull(message = "Patient ID is required")
    val patientId: Long,

    @field:NotNull(message = "Specialist ID is required")
    val specialistId: Long,

    @field:NotNull(message = "Scheduled date/time is required")
    val scheduledAt: LocalDateTime,

    @field:NotBlank(message = "Service type is required")
    val serviceType: String,

    val notes: String? = null
)

data class FitnessAppointmentStatusRequest(
    @field:NotBlank(message = "Status is required")
    val status: String
)

data class FitnessAppointmentResponse(
    val id: Long,
    val patientId: Long,
    val patientFullName: String,
    val specialistId: Long,
    val specialistFullName: String,
    val specialistRole: String,
    val scheduledAt: LocalDateTime,
    val serviceType: String,
    val status: String,
    val notes: String?,
    val createdAt: LocalDateTime
)


// ─── DietPlan ─────────────────────────────────────────────────────────────────

data class DietPlanRequest(
    @field:NotNull(message = "Patient ID is required")
    val patientId: Long,

    @field:NotNull(message = "Specialist ID is required")
    val specialistId: Long,

    @field:NotBlank(message = "Title is required")
    val title: String,

    val description: String? = null,

    @field:Min(value = 500, message = "Calories must be at least 500")
    @field:Max(value = 5000, message = "Calories cannot exceed 5000")
    val calories: Int? = null,

    @field:Min(value = 1, message = "Duration must be at least 1 week")
    @field:Max(value = 52, message = "Duration cannot exceed 52 weeks")
    val durationWeeks: Int? = null,
    val active: Boolean = true
)

data class DietPlanResponse(
    val id: Long,
    val patientId: Long,
    val specialistId: Long,
    val patientFirstName: String,
    val patientLastName: String,
    val specialistFirstName: String,
    val specialistLastName: String,
    val title: String,
    val description: String?,
    val calories: Int?,
    val durationWeeks: Int?,
    val active: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

// ─── TrainingPlan ─────────────────────────────────────────────────────────────

data class TrainingPlanRequest(
    @field:NotNull(message = "Patient ID is required")
    val patientId: Long,

    @field:NotNull(message = "Specialist ID is required")
    val specialistId: Long,

    @field:NotBlank(message = "Title is required")
    val title: String,

    val description: String? = null,

    @field:Min(value = 1, message = "Duration must be at least 1 week")
    @field:Max(value = 52, message = "Duration cannot exceed 52 weeks")
    val weeks: Int? = null,

    @field:Min(value = 1, message = "Sessions per week must be at least 1")
    @field:Max(value = 7, message = "Sessions per week cannot exceed 7")
    val sessionsPerWeek: Int? = null,
    val active: Boolean = true
)

data class TrainingPlanResponse(
    val id: Long,
    val patientId: Long,
    val patientFullName: String,
    val specialistId: Long,
    val specialistFullName: String,
    val title: String,
    val description: String?,
    val weeks: Int?,
    val sessionsPerWeek: Int?,
    val active: Boolean,
    val createdAt: LocalDateTime
)

// ─── Recipe ───────────────────────────────────────────────────────────────────

data class RecipeRequest(
    @field:NotBlank(message = "Title is required")
    val title: String,

    val description: String? = null,
    val ingredients: String? = null,
    val instructions: String? = null,
    val calories: Int? = null,
    val category: String? = null
)

data class RecipeResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val ingredients: String?,
    val instructions: String?,
    val calories: Int?,
    val category: String?,
    val createdAt: LocalDateTime
)

// ─── Dashboard ────────────────────────────────────────────────────────────────

data class DashboardResponse(
    val totalClients: Long,
    val totalAppointments: Long,
    val bookedAppointments: Long,
    val completedAppointments: Long,
    val activeDietPlans: Long,
    val activeTrainingPlans: Long,
    val totalRecipes: Long
)
