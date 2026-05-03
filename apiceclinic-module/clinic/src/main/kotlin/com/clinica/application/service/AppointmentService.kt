package com.clinica.application.service

import com.clinic.model.AppointmentRequest
import com.clinic.model.AppointmentStatusRequest
import com.clinica.application.domain.Appointment
import com.clinica.application.domain.AppointmentStatusEnum
import com.clinica.doors.outbound.database.dao.AppointmentDao
import com.clinica.doors.outbound.database.dao.PatientDao
import com.clinica.doors.outbound.database.dao.SpecialistDao
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
    ): List<Appointment> =
        appointmentDao.findAll(patientId, specialist, status)

    @Transactional(readOnly = true)
    fun findById(id: Long): Appointment =
        appointmentDao.findById(id).orThrow("Appointment not found with id: $id")

    @Transactional
    fun create(request: AppointmentRequest): Appointment {
        val patient = patientDao.findById(request.patientId)
            .orThrow("Patient not found with id: ${request.patientId}")

        val specialist = specialistDao.findById(request.specialistId)
            .orThrow("Specialist not found with id: ${request.specialistId}")

        val appointment = Appointment(
            id = 0L,
            patient = patient,
            specialist = specialist,
            scheduledAt = request.scheduledAt.toLocalDateTime(),
            visitType = request.visitType,
            status = AppointmentStatusEnum.BOOKED,
            notes = request.notes,
            updatedAt = LocalDateTime.now()
        )

        return appointmentDao.save(appointment)
    }

    @Transactional
    fun updateStatus(id: Long, request: AppointmentStatusRequest): Appointment {
        val appointment = appointmentDao.findById(id)
            .orThrow("Appointment not found with id: $id")
        val updated = appointment.copy(
            status = AppointmentStatusEnum.parse(request.status),
            updatedAt = LocalDateTime.now()
        )
        return appointmentDao.save(updated)
    }

    @Transactional
    fun delete(id: Long) {
        appointmentDao.findById(id).orThrow("Appointment not found with id: $id")
        appointmentDao.deleteById(id)
    }
}