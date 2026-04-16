package com.clinica.service

import com.clinica.domain.AppointmentEntity
import com.clinica.domain.AppointmentStatus
import com.clinica.dto.AppointmentRequest
import com.clinica.dto.AppointmentResponse
import com.clinica.dto.AppointmentStatusRequest
import com.clinica.repository.AppointmentRepository
import com.clinica.repository.DoctorRepository
import com.clinica.repository.PatientRepository
import com.clinica.util.orEntityNotFound
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AppointmentService(
    private val appointmentRepository: AppointmentRepository,
    private val patientRepository: PatientRepository,
    private val doctorRepository: DoctorRepository
) {

    @Transactional(readOnly = true)
    fun findAll(patientId: Long?, doctorId: Long?, status: String?): List<AppointmentResponse> {
        val appointments = when {
            patientId != null -> appointmentRepository.findByPatientId(patientId)
            doctorId != null  -> appointmentRepository.findByDoctorId(doctorId)
            status != null    -> appointmentRepository.findByStatus(AppointmentStatus.valueOf(status.uppercase()))
            else              -> appointmentRepository.findAll()
        }
        return appointments.map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): AppointmentResponse =
        appointmentRepository.findById(id).orEntityNotFound("Appointment", id).toResponse()

    fun create(request: AppointmentRequest): AppointmentResponse {
        val patient = patientRepository.findById(request.patientId)
            .orEntityNotFound("Patient", request.patientId)
        val doctor = doctorRepository.findById(request.doctorId)
            .orEntityNotFound("Doctor", request.doctorId)

        val appointmentEntity = AppointmentEntity(
            patientEntity = patient,
            doctor = doctor,
            scheduledAt = request.scheduledAt,
            visitType = request.visitType,
            notes = request.notes
        )
        return appointmentRepository.save(appointmentEntity).toResponse()
    }

    fun updateStatus(id: Long, request: AppointmentStatusRequest): AppointmentResponse {
        val appointment = appointmentRepository.findById(id).orEntityNotFound("Appointment", id)

        val newStatus = try {
            AppointmentStatus.valueOf(request.status.uppercase())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException(
                "Invalid status '${request.status}'. Valid values: ${AppointmentStatus.values().joinToString()}"
            )
        }

        appointment.status = newStatus
        return appointmentRepository.save(appointment).toResponse()
    }

    fun delete(id: Long) {
        val appointment = appointmentRepository.findById(id).orEntityNotFound("Appointment", id)
        appointment.status = AppointmentStatus.CANCELLED
        appointmentRepository.save(appointment)
    }

    private fun AppointmentEntity.toResponse() = AppointmentResponse(
        id = id,
        patientId = patientEntity.id,
        patientFullName = "${patientEntity.firstName} ${patientEntity.lastName}",
        doctorId = doctor.id,
        doctorFullName = "${doctor.firstName} ${doctor.lastName}",
        doctorSpecialization = doctor.specialization,
        scheduledAt = scheduledAt,
        visitType = visitType,
        status = status.name,
        notes = notes,
        hasReport = report != null,
        createdAt = createdAt
    )
}
