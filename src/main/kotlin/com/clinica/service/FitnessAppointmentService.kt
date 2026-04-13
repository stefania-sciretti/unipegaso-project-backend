package com.clinica.service

import com.clinica.domain.AppointmentStatus
import com.clinica.domain.FitnessAppointment
import com.clinica.dto.FitnessAppointmentRequest
import com.clinica.dto.FitnessAppointmentResponse
import com.clinica.dto.FitnessAppointmentStatusRequest
import com.clinica.repository.ClientRepository
import com.clinica.repository.FitnessAppointmentRepository
import com.clinica.repository.TrainerRepository
import com.clinica.service.api.FitnessAppointmentServicePort
import com.clinica.util.orEntityNotFound
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class FitnessAppointmentService(
    private val appointmentRepository: FitnessAppointmentRepository,
    private val clientRepository: ClientRepository,
    private val trainerRepository: TrainerRepository
) : FitnessAppointmentServicePort {

    @Transactional(readOnly = true)
    override fun findAll(clientId: Long?, trainerId: Long?, status: String?): List<FitnessAppointmentResponse> {
        val appointments = when {
            clientId != null  -> appointmentRepository.findByClientId(clientId)
            trainerId != null -> appointmentRepository.findByTrainerId(trainerId)
            status != null    -> appointmentRepository.findByStatus(AppointmentStatus.valueOf(status.uppercase()))
            else              -> appointmentRepository.findAll()
        }
        return appointments.map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): FitnessAppointmentResponse =
        appointmentRepository.findById(id).orEntityNotFound("Appointment", id).toResponse()

    override fun create(request: FitnessAppointmentRequest): FitnessAppointmentResponse {
        val client = clientRepository.findById(request.clientId)
            .orEntityNotFound("Client", request.clientId)
        val trainer = trainerRepository.findById(request.trainerId)
            .orEntityNotFound("Trainer", request.trainerId)

        val appointment = FitnessAppointment(
            client = client,
            trainer = trainer,
            scheduledAt = request.scheduledAt,
            serviceType = request.serviceType,
            notes = request.notes
        )
        return appointmentRepository.save(appointment).toResponse()
    }

    override fun updateStatus(id: Long, request: FitnessAppointmentStatusRequest): FitnessAppointmentResponse {
        val appointment = appointmentRepository.findById(id).orEntityNotFound("Appointment", id)
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
        val appointment = appointmentRepository.findById(id).orEntityNotFound("Appointment", id)
        appointment.status = AppointmentStatus.CANCELLED
        appointmentRepository.save(appointment)
    }

    private fun FitnessAppointment.toResponse() = FitnessAppointmentResponse(
        id = id,
        clientId = client.id,
        clientFullName = "${client.firstName} ${client.lastName}",
        trainerId = trainer.id,
        trainerFullName = "${trainer.firstName} ${trainer.lastName}",
        trainerRole = trainer.role.name,
        scheduledAt = scheduledAt,
        serviceType = serviceType,
        status = status.name,
        notes = notes,
        createdAt = createdAt
    )
}
