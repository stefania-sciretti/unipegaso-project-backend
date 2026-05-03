package com.clinica.application.service

import com.clinica.application.domain.Appointment
import com.clinica.application.domain.AppointmentStatusEnum
import com.clinica.application.domain.Report
import com.clinica.doors.outbound.database.dao.FitnessAppointmentDao
import com.clinica.doors.outbound.database.dao.ReportDao
import com.clinic.model.ReportRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class ReportService(
    private val reportDao: ReportDao,
    private val fitnessAppointmentDao: FitnessAppointmentDao
) {

    @Transactional(readOnly = true)
    fun findAll(): List<Report> =
        reportDao.findAll()

    @Transactional(readOnly = true)
    fun findById(id: Long): Report =
        reportDao.findById(id).orThrow("Report $id not found")

    @Transactional(readOnly = true)
    fun findByAppointmentId(appointmentId: Long): Report =
        reportDao.findByAppointmentId(appointmentId)
            .orThrow("Report for appointment $appointmentId not found")

    @Transactional
    fun create(request: ReportRequest): Report {
        val fitnessAppointment = fitnessAppointmentDao.findById(request.appointmentId)
            ?: throw NoSuchElementException("Appointment ${request.appointmentId} not found")

        check(fitnessAppointment.status == AppointmentStatusEnum.COMPLETED) {
            "Report can only be created for COMPLETED appointments"
        }

        check(reportDao.findByAppointmentId(request.appointmentId) == null) {
            "Report already exists for appointment ${request.appointmentId}"
        }

        val now = LocalDateTime.now()
        val report = Report(
            id = 0L,
            appointment = Appointment(
                id = fitnessAppointment.id,
                patient = fitnessAppointment.patient,
                specialist = fitnessAppointment.specialist,
                scheduledAt = fitnessAppointment.scheduledAt,
                visitType = fitnessAppointment.serviceType,
                status = fitnessAppointment.status,
                notes = fitnessAppointment.notes
            ),
            issuedDate = LocalDate.now(),
            diagnosis = request.diagnosis,
            prescription = request.prescription,
            specialistNotes = request.specialistNotes,
            createdAt = now,
            updatedAt = now
        )
        return reportDao.save(report)
    }

    @Transactional
    fun update(id: Long, request: ReportRequest): Report {
        val existing = reportDao.findById(id).orThrow("Report $id not found")
        val updated = existing.copy(
            diagnosis = request.diagnosis,
            prescription = request.prescription,
            specialistNotes = request.specialistNotes,
            updatedAt = LocalDateTime.now()
        )
        return reportDao.save(updated)
    }
}