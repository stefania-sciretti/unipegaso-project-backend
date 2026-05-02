package com.clinica.application.service

import com.clinica.application.domain.Appointment
import com.clinica.application.domain.AppointmentStatusEnum
import com.clinica.doors.outbound.database.dao.AppointmentDao
import com.clinica.doors.outbound.database.dao.PatientDao
import com.clinica.doors.outbound.database.dao.SpecialistDao
import com.clinica.dto.AppointmentRequest
import com.clinic.model.AppointmentResponse
import com.clinica.dto.AppointmentStatusRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class AppointmentService(
    private val appointmentDao: AppointmentDao,
    private val patientDao: PatientDao,
    private val specialistDao: SpecialistDao,
) {

    @Transactional(readOnly = true)
    fun findAll(
        patientId: Long?,
        specialist: Long?,
        status: AppointmentStatusEnum?
    ): List<AppointmentResponse> =
        appointmentDao.findAll(patientId, specialist, status)
            .map { it.toResponse() }

    @Transactional(readOnly = true)
    fun findById(id: Long): AppointmentResponse =
        appointmentDao.findById(id).orThrow("Appointment not found with id: $id").toResponse()

    @Transactional
    fun create(request: AppointmentRequest): AppointmentResponse {
        val patient = patientDao.findById(request.patientId)
            .orThrow("Patient not found with id: ${request.patientId}")

        val specialist = specialistDao.findById(request.specialistId)
            .orThrow("Specialist not found with id: ${request.specialistId}")

        val appointment = Appointment(
            id = 0L,
            patient = patient,
            specialist = specialist,
            scheduledAt = request.scheduledAt,
            visitType = request.visitType,
            status = AppointmentStatusEnum.BOOKED,
            notes = request.notes,
            updatedAt = LocalDateTime.now()
        )

        return appointmentDao.save(appointment).toResponse()
    }

    @Transactional
    fun updateStatus(id: Long, request: AppointmentStatusRequest): AppointmentResponse {
        val appointment = appointmentDao.findById(id)
            .orThrow("Appointment not found with id: $id")
        val updated = appointment.copy(
            status = AppointmentStatusEnum.parse(request.status),
            updatedAt = LocalDateTime.now()
        )
        return appointmentDao.save(updated).toResponse()
    }

    @Transactional
    fun delete(id: Long) {
        appointmentDao.findById(id).orThrow("Appointment not found with id: $id")
        appointmentDao.deleteById(id)
    }
}