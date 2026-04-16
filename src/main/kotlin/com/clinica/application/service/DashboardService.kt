package com.clinica.service

import com.clinica.application.domain.AppointmentStatus
import com.clinica.dto.DashboardResponse
import com.clinica.repository.ClientRepository
import com.clinica.repository.DietPlanRepository
import com.clinica.repository.FitnessAppointmentRepository
import com.clinica.repository.RecipeRepository
import com.clinica.repository.TrainingPlanRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class DashboardService(
    private val clientRepository: ClientRepository,
    private val appointmentRepository: FitnessAppointmentRepository,
    private val dietPlanRepository: DietPlanRepository,
    private val trainingPlanRepository: TrainingPlanRepository,
    private val recipeRepository: RecipeRepository
) {
    fun getDashboard(): DashboardResponse = DashboardResponse(
        totalClients = clientRepository.count(),
        totalAppointments = appointmentRepository.count(),
        bookedAppointments = appointmentRepository.countByStatus(AppointmentStatus.BOOKED),
        completedAppointments = appointmentRepository.countByStatus(AppointmentStatus.COMPLETED),
        activeDietPlans = dietPlanRepository.countByActiveTrue(),
        activeTrainingPlans = trainingPlanRepository.countByActiveTrue(),
        totalRecipes = recipeRepository.count()
    )
}
