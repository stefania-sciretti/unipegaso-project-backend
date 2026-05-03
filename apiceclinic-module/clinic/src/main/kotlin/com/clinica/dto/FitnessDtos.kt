package com.clinica.dto

data class DashboardResponse(
    val totalClients: Long,
    val totalAppointments: Long,
    val bookedAppointments: Long,
    val completedAppointments: Long,
    val activeDietPlans: Long,
    val activeTrainingPlans: Long,
    val totalRecipes: Long
)
