package com.clinica.application.service

import com.clinica.application.domain.Appointment
import com.clinica.application.domain.AppointmentStatus
import com.clinica.doors.outbound.database.dao.AppointmentDao
import com.clinica.doors.outbound.database.dao.DoctorDao
import com.clinica.doors.outbound.database.dao.PatientDao
import com.clinica.dto.AppointmentRequest
import com.clinica.dto.AppointmentResponse
import com.clinica.dto.AppointmentStatusRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class AppointmentService(
    private val appointmentDao: AppointmentDao,
    private val patientDao: PatientDao,
    private val doctorDao: DoctorDao
) {

    @Transactional(readOnly = true)
    fun findAll(
        patientId: Long?,
        doctorId: Long?,
        status: String?
    ): List<AppointmentResponse> =
        appointmentDao.findAll(patientId, doctorId, status)
            .map { it.toResponse() }

    @Transactional(readOnly = true)
    fun findById(id: Long): AppointmentResponse =
        appointmentDao.findById(id)?.toResponse()
            ?: throw NoSuchElementException("Appointment not found with id: $id")

    fun create(request: AppointmentRequest): AppointmentResponse {
        // carico il dominio Patient/Doctor per popolare l’Appointment di dominio
        val patient = patientDao.findById(request.patientId)
            ?: throw NoSuchElementException("Patient not found with id: ${request.patientId}")

        val doctor = doctorDao.findById(request.doctorId)
            ?: throw NoSuchElementException("Doctor not found with id: ${request.doctorId}")

        val appointment = Appointment(
            id = 0L,
            patient = patient,
            doctor = doctor,
            scheduledAt = request.scheduledAt,
            visitType = request.visitType,
            status = AppointmentStatus.BOOKED,
            notes = request.notes,
            updatedAt = LocalDateTime.now(),
            report = null
        )

        return appointmentDao.save(appointment).toResponse()
    }

    fun updateStatus(id: Long, request: AppointmentStatusRequest): AppointmentResponse {
        val appointment = appointmentDao.findById(id)
            ?: throw NoSuchElementException("Appointment not found with id: $id")

        val newStatus = try {
            AppointmentStatus.valueOf(request.status.uppercase())
        } catch (ex: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid appointment status: '${request.status}'")
        }

        val updated = appointment.copy(
            status = newStatus,
            updatedAt = LocalDateTime.now()
        )

        return appointmentDao.save(updated).toResponse()
    }

    fun delete(id: Long) {
        if (!appointmentDao.existsById(id)) {
            throw NoSuchElementException("Appointment not found with id: $id")
        }
        appointmentDao.deleteById(id)
    }

    // mapper dominio -> response
    private fun Appointment.toResponse(): AppointmentResponse =
        AppointmentResponse(
            id = this.id,
            patientId = this.patient.id,
            patientFullName = "${this.patient.firstName} ${this.patient.lastName}",
            doctorId = this.doctor.id,
            doctorFullName = "${this.doctor.firstName} ${this.doctor.lastName}",
            doctorSpecialization = this.doctor.specialization,
            scheduledAt = this.scheduledAt,
            visitType = this.visitType,
            status = this.status.name,
            notes = this.notes,
            hasReport = this.report != null,
            createdAt = this.updatedAt
        )
}