package com.clinica.application.service

import com.clinica.application.domain.AppointmentStatus
import com.clinica.doors.outbound.database.repositories.AppointmentRepository
import com.clinica.doors.outbound.database.repositories.DietPlanRepository
import com.clinica.doors.outbound.database.repositories.FitnessAppointmentRepository
import com.clinica.doors.outbound.database.repositories.PatientRepository
import com.clinica.doors.outbound.database.repositories.RecipeRepository
import com.clinica.doors.outbound.database.repositories.TrainingPlanRepository
import com.clinica.dto.DashboardResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class DashboardService(
    private val patientRepository: PatientRepository,
    private val appointmentRepository: AppointmentRepository,
    private val dietPlanRepository: DietPlanRepository,
    private val trainingPlanRepository: TrainingPlanRepository,
    private val recipeRepository: RecipeRepository,
    private val fitnessAppointmentRepository: FitnessAppointmentRepository
) {
    fun getDashboard(): DashboardResponse = DashboardResponse(
        totalClients = patientRepository.count(),
        totalAppointments = appointmentRepository.count(),
        bookedAppointments = appointmentRepository.countByStatus(AppointmentStatus.BOOKED.name),
        completedAppointments = appointmentRepository.countByStatus(AppointmentStatus.COMPLETED.name),
        activeDietPlans = dietPlanRepository.countByActiveTrue(),
        activeTrainingPlans = trainingPlanRepository.countByActiveTrue(),
        totalRecipes = recipeRepository.count()
    )
}
