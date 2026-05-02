package com.clinica.application.service

import com.clinica.application.domain.Appointment
import com.clinica.application.domain.AppointmentStatusEnum
import com.clinica.doors.outbound.database.dao.AppointmentDao
import com.clinica.doors.outbound.database.dao.PatientDao
import com.clinica.doors.outbound.database.dao.SpecialistDao
import com.clinica.dto.AppointmentRequest
import com.clinica.dto.AppointmentResponse
import com.clinica.dto.AppointmentStatusRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AppointmentService(
    private val appointmentDao: AppointmentDao,
    private val patientDao: PatientDao,
    private val specialistDao: SpecialistDao,
) {

    fun findAll(
        patientId: Long?,
        specialist: Long?,
        status: AppointmentStatusEnum?
    ): List<AppointmentResponse> =
        appointmentDao.findAll(patientId, specialist, status)
            .map { it.toResponse() }

    fun findById(id: Long): AppointmentResponse =
        appointmentDao.findById(id).orThrow("Appointment not found with id: $id").toResponse()

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

    fun updateStatus(id: Long, request: AppointmentStatusRequest): AppointmentResponse {
        val appointment = appointmentDao.findById(id)
            .orThrow("Appointment not found with id: $id")
        val updated = appointment.copy(
            status = AppointmentStatusEnum.parse(request.status),
            updatedAt = LocalDateTime.now()
        )
        return appointmentDao.save(updated).toResponse()
    }

    fun delete(id: Long) {
        appointmentDao.findById(id).orThrow("Appointment not found with id: $id")
        appointmentDao.deleteById(id)
    }

    private fun Appointment.toResponse(): AppointmentResponse =
        AppointmentResponse(
            id = this.id,
            patientId = this.patient.id,
            patientFullName = this.patient.fullName,
            specialistId = this.specialist.id,
            specialistFullName = this.specialist.fullName,
            specialistSpecialization = this.specialist.role,
            scheduledAt = this.scheduledAt,
            visitType = this.visitType,
            status = this.status.name,
            notes = this.notes,
            hasReport = false,
            createdAt = this.updatedAt
        )
}