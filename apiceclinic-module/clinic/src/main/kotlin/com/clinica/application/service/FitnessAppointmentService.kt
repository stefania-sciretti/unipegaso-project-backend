package com.clinica.application.service

import com.clinica.application.domain.AppointmentStatus
import com.clinica.application.domain.FitnessAppointment
import com.clinica.doors.outbound.database.dao.FitnessAppointmentDao
import com.clinica.doors.outbound.database.dao.PatientDao
import com.clinica.doors.outbound.database.dao.StaffDao
import com.clinica.dto.FitnessAppointmentRequest
import com.clinica.dto.FitnessAppointmentResponse
import com.clinica.dto.FitnessAppointmentStatusRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class FitnessAppointmentService(
    private val appointmentDao: FitnessAppointmentDao,
    private val clientDao: PatientDao,
    private val staffDao: StaffDao
) {

    @Transactional(readOnly = true)
    fun findAll(clientId: Long?, trainerId: Long?, status: String?): List<FitnessAppointmentResponse> =
        appointmentDao.findAll(clientId, trainerId, status).map { it.toResponse() }

    @Transactional(readOnly = true)
    fun findById(id: Long): FitnessAppointmentResponse =
        appointmentDao.findById(id)?.toResponse()
            ?: throw NoSuchElementException("Fitness appointment not found with id: $id")

    fun create(request: FitnessAppointmentRequest): FitnessAppointmentResponse {
        val client = clientDao.findById(request.clientId)
            ?: throw NoSuchElementException("Client not found with id: ${request.clientId}")
        val staff = staffDao.findById(request.trainerId)
            ?: throw NoSuchElementException("Staff not found with id: ${request.trainerId}")

        val appointment = FitnessAppointment(
            id = 0L,
            client = client,
            staff = staff,
            scheduledAt = request.scheduledAt,
            serviceType = request.serviceType,
            status = AppointmentStatus.BOOKED,
            notes = request.notes,
            updatedAt = LocalDateTime.now()
        )
        return appointmentDao.save(appointment).toResponse()
    }

    fun updateStatus(id: Long, request: FitnessAppointmentStatusRequest): FitnessAppointmentResponse {
        val appointment = appointmentDao.findById(id)
            ?: throw NoSuchElementException("Fitness appointment not found with id: $id")
        val newStatus = try {
            AppointmentStatus.valueOf(request.status.uppercase())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid status '${request.status}'")
        }
        val updated = appointment.copy(status = newStatus, updatedAt = LocalDateTime.now())
        return appointmentDao.save(updated).toResponse()
    }

    fun delete(id: Long) {
        val appointment = appointmentDao.findById(id)
            ?: throw NoSuchElementException("Fitness appointment not found with id: $id")
        val cancelled = appointment.copy(status = AppointmentStatus.CANCELLED)
        appointmentDao.save(cancelled)
    }

    private fun FitnessAppointment.toResponse() = FitnessAppointmentResponse(
        id = id,
        clientId = client.id,
        clientFullName = "${client.firstName} ${client.lastName}",
        trainerId = staff.id,
        trainerFullName = "${staff.firstName} ${staff.lastName}",
        trainerRole = staff.role,
        scheduledAt = scheduledAt,
        serviceType = serviceType,
        status = status.name,
        notes = notes,
        createdAt = updatedAt
    )
}
