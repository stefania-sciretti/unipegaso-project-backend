package com.clinica.service.api

import com.clinica.dto.FitnessAppointmentRequest
import com.clinica.dto.FitnessAppointmentResponse
import com.clinica.dto.FitnessAppointmentStatusRequest

/**
 * Contract for fitness appointment management operations.
 */
interface FitnessAppointmentServicePort {
    fun findAll(clientId: Long?, trainerId: Long?, status: String?): List<FitnessAppointmentResponse>
    fun findById(id: Long): FitnessAppointmentResponse
    fun create(request: FitnessAppointmentRequest): FitnessAppointmentResponse
    fun updateStatus(id: Long, request: FitnessAppointmentStatusRequest): FitnessAppointmentResponse
    fun delete(id: Long)
}
