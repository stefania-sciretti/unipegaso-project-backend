package com.clinica.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate
import java.time.LocalDateTime

// ─── Client ──────────────────────────────────────────────────────────────────

data class ClientRequest(
    @field:NotBlank(message = "First name is required")
    val firstName: String,

    @field:NotBlank(message = "Last name is required")
    val lastName: String,

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String,

    val phone: String? = null,
    val birthDate: LocalDate? = null,
    val goal: String? = null
)

data class ClientResponse(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String?,
    val birthDate: LocalDate?,
    val goal: String?,
    val createdAt: LocalDateTime
)

// ─── Staff ─────────────────────────────────────────────────────────────────

data class StaffResponse(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val role: String,
    val bio: String?,
    val email: String,
    val createdAt: LocalDateTime
)

// ─── FitnessAppointment ───────────────────────────────────────────────────────

data class FitnessAppointmentRequest(
    @field:NotNull(message = "Client ID is required")
    val clientId: Long,

    @field:NotNull(message = "Trainer ID is required")
    val trainerId: Long,

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
    val clientId: Long,
    val clientFullName: String,
    val trainerId: Long,
    val trainerFullName: String,
    val trainerRole: String,
    val scheduledAt: LocalDateTime,
    val serviceType: String,
    val status: String,
    val notes: String?,
    val createdAt: LocalDateTime
)


// ─── DietPlan ─────────────────────────────────────────────────────────────────

data class DietPlanRequest(
    @field:NotNull(message = "Client ID is required")
    val clientId: Long,

    @field:NotNull(message = "Trainer ID is required")
    val trainerId: Long,

    @field:NotBlank(message = "Title is required")
    val title: String,

    val description: String? = null,
    val calories: Int? = null,
    val durationWeeks: Int? = null,
    val active: Boolean = true
)

data class DietPlanResponse(
    val id: Long,
    val clientId: Long,
    val trainerId: Long,
    val clientFirstName: String,
    val clientLastName: String,
    val trainerFirstName: String,
    val trainerLastName: String,
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
    @field:NotNull(message = "Client ID is required")
    val clientId: Long,

    @field:NotNull(message = "Trainer ID is required")
    val trainerId: Long,

    @field:NotBlank(message = "Title is required")
    val title: String,

    val description: String? = null,
    val weeks: Int? = null,
    val sessionsPerWeek: Int? = null,
    val active: Boolean = true
)

data class TrainingPlanResponse(
    val id: Long,
    val clientId: Long,
    val clientFullName: String,
    val trainerId: Long,
    val trainerFullName: String,
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
