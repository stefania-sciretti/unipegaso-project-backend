package com.clinica.application.service

import com.clinica.application.domain.AppointmentStatusEnum
import com.clinica.doors.outbound.database.repositories.*
import com.clinica.dto.DashboardResponse
import org.springframework.stereotype.Service

@Service
class DashboardService(
    private val patientRepository: PatientRepository,
    private val appointmentRepository: AppointmentRepository,
    private val dietPlanRepository: DietPlanRepository,
    private val trainingPlanRepository: TrainingPlanRepository,
    private val recipeRepository: RecipeRepository,
) {
    fun getDashboard(): DashboardResponse = DashboardResponse(
        totalClients = patientRepository.count(),
        totalAppointments = appointmentRepository.count(),
        bookedAppointments = appointmentRepository.countByStatus(AppointmentStatusEnum.BOOKED.name),
        completedAppointments = appointmentRepository.countByStatus(AppointmentStatusEnum.COMPLETED.name),
        activeDietPlans = dietPlanRepository.countByActiveTrue(),
        activeTrainingPlans = trainingPlanRepository.countByActiveTrue(),
        totalRecipes = recipeRepository.count()
    )
}
