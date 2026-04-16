package com.clinica.fitnessappointment.application.services

import com.clinica.fitnessappointment.application.domain.AppointmentStatus
import com.clinica.fitnessappointment.application.domain.FitnessAppointment
import com.clinica.fitnessappointment.application.services.api.FitnessAppointmentServicePort
import com.clinica.fitnessappointment.doors.outbound.database.FitnessAppointmentRepository
import com.clinica.fitnessappointment.doors.outbound.database.ClientRepository
import com.clinica.fitnessappointment.doors.outbound.database.StaffRepository
import com.clinica.fitnessappointment.application.domain.*
import com.clinica.fitnessappointment.application.domain.dto.FitnessAppointmentRequest
import com.clinica.fitnessappointment.application.domain.dto.FitnessAppointmentResponse
import com.clinica.fitnessappointment.application.domain.dto.FitnessAppointmentStatusRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class FitnessAppointmentService(
    private val appointmentRepository: FitnessAppointmentRepository,
    private val clientRepository: ClientRepository,
    private val staffRepository: StaffRepository
) : FitnessAppointmentServicePort {

    @Transactional(readOnly = true)
    override fun findAll(clientId: Long?, trainerId: Long?, status: String?): List<FitnessAppointmentResponse> {
        val appointments = when {
            clientId != null  -> appointmentRepository.findByClientId(clientId)
            trainerId != null -> appointmentRepository.findByStaffId(trainerId)
            status != null    -> appointmentRepository.findByStatus(AppointmentStatus.valueOf(status.uppercase()))
            else              -> appointmentRepository.findAll()
        }
        return appointments.map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): FitnessAppointmentResponse =
        appointmentRepository.findById(id)?.toResponse() ?: throw NoSuchElementException("Appointment not found with id: $id")

    override fun create(request: FitnessAppointmentRequest): FitnessAppointmentResponse {
        val client = clientRepository.findById(request.clientId) ?: throw NoSuchElementException("Client not found with id: ${request.clientId}")
        val staff = staffRepository.findById(request.trainerId) ?: throw NoSuchElementException("Staff not found with id: ${request.trainerId}")

        val appointment = FitnessAppointment(
            clientEntity = client,
            staff = staff,
            scheduledAt = request.scheduledAt,
            serviceType = request.serviceType,
            notes = request.notes
        )
        return appointmentRepository.save(appointment).toResponse()
    }

    override fun updateStatus(id: Long, request: FitnessAppointmentStatusRequest): FitnessAppointmentResponse {
        val appointment = appointmentRepository.findById(id) ?: throw NoSuchElementException("Appointment not found with id: $id")
        val newStatus = try {
            AppointmentStatus.valueOf(request.status.uppercase())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException(
                "Invalid status '${request.status}'. Valid values: ${AppointmentStatus.entries.joinToString()}"
            )
        }
        appointment.status = newStatus
        return appointmentRepository.save(appointment).toResponse()
    }

    override fun delete(id: Long) {
        val appointment = appointmentRepository.findById(id) ?: throw NoSuchElementException("Appointment not found with id: $id")
        appointment.status = AppointmentStatus.CANCELLED
        appointmentRepository.save(appointment)
    }

    private fun FitnessAppointment.toResponse() = FitnessAppointmentResponse(
        id = id,
        clientId = clientEntity.id,
        clientFullName = "${clientEntity.firstName} ${clientEntity.lastName}",
        trainerId = staff.id,
        trainerFullName = "${staff.firstName} ${staff.lastName}",
        trainerRole = staff.role,
        scheduledAt = scheduledAt,
        serviceType = serviceType,
        status = status.name,
        notes = notes,
        createdAt = createdAt
    )
}
