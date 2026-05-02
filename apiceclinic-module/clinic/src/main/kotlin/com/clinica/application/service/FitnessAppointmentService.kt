package com.clinica.application.service

import com.clinica.application.domain.AppointmentStatusEnum
import com.clinica.application.domain.FitnessAppointment
import com.clinica.doors.outbound.database.dao.FitnessAppointmentDao
import com.clinica.doors.outbound.database.dao.PatientDao
import com.clinica.doors.outbound.database.dao.SpecialistDao
import com.clinica.dto.FitnessAppointmentRequest
import com.clinic.model.FitnessAppointmentResponse
import com.clinica.dto.FitnessAppointmentStatusRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class FitnessAppointmentService(
    private val appointmentDao: FitnessAppointmentDao,
    private val patientDao: PatientDao,
    private val specialistDao: SpecialistDao
) {

    @Transactional(readOnly = true)
    fun findAll(patientId: Long?, specialistId: Long?, status: String?): List<FitnessAppointmentResponse> =
        appointmentDao.findAll(patientId, specialistId, status).map { it.toResponse() }

    @Transactional(readOnly = true)
    fun findById(id: Long): FitnessAppointmentResponse =
        appointmentDao.findById(id).orThrow("Fitness appointment not found with id: $id").toResponse()

    @Transactional
    fun create(request: FitnessAppointmentRequest): FitnessAppointmentResponse {
        val patient = patientDao.findById(request.patientId)
            .orThrow("Patient not found with id: ${request.patientId}")
        val specialist = specialistDao.findById(request.specialistId)
            .orThrow("Specialist not found with id: ${request.specialistId}")

        val appointment = FitnessAppointment(
            id = 0L,
            patient = patient,
            specialist = specialist,
            scheduledAt = request.scheduledAt,
            serviceType = request.serviceType,
            status = AppointmentStatusEnum.BOOKED,
            notes = request.notes,
            updatedAt = LocalDateTime.now()
        )
        return appointmentDao.save(appointment).toResponse()
    }

    @Transactional
    fun updateStatus(id: Long, request: FitnessAppointmentStatusRequest): FitnessAppointmentResponse {
        val appointment = appointmentDao.findById(id)
            .orThrow("Fitness appointment not found with id: $id")
        val updated = appointment.copy(
            status = AppointmentStatusEnum.parse(request.status),
            updatedAt = LocalDateTime.now()
        )
        return appointmentDao.save(updated).toResponse()
    }

    @Transactional
    fun delete(id: Long) {
        val appointment = appointmentDao.findById(id)
            .orThrow("Fitness appointment not found with id: $id")
        appointmentDao.save(appointment.copy(status = AppointmentStatusEnum.CANCELLED))
    }
}
