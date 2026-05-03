package com.clinica.application.service

import com.clinic.model.AppointmentRequest
import com.clinic.model.AppointmentStatusRequest
import com.clinica.application.domain.Appointment
import com.clinica.application.domain.AppointmentStatusEnum
import com.clinica.doors.outbound.database.dao.AppointmentDao
import com.clinica.doors.outbound.database.dao.PatientDao
import com.clinica.doors.outbound.database.dao.ReportDao
import com.clinica.doors.outbound.database.dao.SpecialistDao
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class AppointmentService(
    private val appointmentDao: AppointmentDao,
    private val patientDao: PatientDao,
    private val specialistDao: SpecialistDao,
    private val reportDao: ReportDao,
) {

    @Transactional(readOnly = true)
    fun findAll(patientId: Long?, specialistId: Long?, status: String?): List<Appointment> {
        val appointments = appointmentDao.findAll(patientId, specialistId, status?.let { AppointmentStatusEnum.parse(it) })
        if (appointments.isEmpty()) return emptyList()
        val withReport = reportDao.findAppointmentIdsWithReports(appointments.map { it.id })
        return appointments.map { it.copy(hasReport = it.id in withReport) }
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): Appointment {
        val appointment = appointmentDao.findById(id)
            ?: throw NoSuchElementException("Appointment not found with id: $id")
        return appointment.copy(hasReport = reportDao.existsByAppointmentId(appointment.id))
    }

    @Transactional
    fun create(request: AppointmentRequest): Appointment {
        val patient = patientDao.findById(request.patientId)
            ?: throw NoSuchElementException("Patient not found with id: ${request.patientId}")

        val specialist = specialistDao.findById(request.specialistId)
            ?: throw NoSuchElementException("Specialist not found with id: ${request.specialistId}")

        val now = LocalDateTime.now()
        return appointmentDao.save(
            Appointment(
                id = 0L,
                patient = patient,
                specialist = specialist,
                scheduledAt = request.scheduledAt.toLocalDateTime(),
                serviceType = request.serviceType,
                status = AppointmentStatusEnum.BOOKED,
                notes = request.notes,
                price = request.price?.let { BigDecimal.valueOf(it) } ?: BigDecimal.ZERO,
                createdAt = now,
                updatedAt = now
            )
        )
    }

    @Transactional
    fun updateStatus(id: Long, request: AppointmentStatusRequest): Appointment {
        val appointment = appointmentDao.findById(id)
            ?: throw NoSuchElementException("Appointment not found with id: $id")
        val updated = appointmentDao.save(
            appointment.copy(
                status = AppointmentStatusEnum.parse(request.status),
                updatedAt = LocalDateTime.now()
            )
        )
        return updated.copy(hasReport = reportDao.existsByAppointmentId(updated.id))
    }

    @Transactional
    fun delete(id: Long) {
        val appointment = appointmentDao.findById(id)
            ?: throw NoSuchElementException("Appointment not found with id: $id")
        appointmentDao.save(
            appointment.copy(
                status = AppointmentStatusEnum.CANCELLED,
                updatedAt = LocalDateTime.now()
            )
        )
    }
}
