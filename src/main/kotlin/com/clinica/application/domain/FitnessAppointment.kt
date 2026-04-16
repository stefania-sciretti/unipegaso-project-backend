package com.clinica.fitnessappointment.application.domain

import java.time.LocalDateTime

// Dominio puro: la classe rappresenta l'appuntamento fitness senza annotazioni JPA.
data class FitnessAppointment(
    val id: Long = 0,
    var client: Client,
    var staff: Staff,
    var scheduledAt: LocalDateTime,
    var serviceType: String,
    var status: AppointmentStatus = AppointmentStatus.BOOKED,
    var notes: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    fun onUpdate() { updatedAt = LocalDateTime.now() }
}
// Nota: la versione JPA è in doors/outbound/database/entities/FitnessAppointmentEntity.kt
