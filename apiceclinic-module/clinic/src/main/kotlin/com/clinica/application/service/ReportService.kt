package com.clinica.application.service

import com.clinic.model.ReportResponse
import com.clinica.application.domain.AppointmentStatusEnum
import com.clinica.application.mappers.toResponse
import com.clinica.doors.outbound.database.dao.ReportDao
import com.clinica.doors.outbound.database.entities.ReportEntity
import com.clinica.doors.outbound.database.repositories.FitnessAppointmentRepository
import com.clinica.dto.ReportRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class ReportService(
    private val reportDao: ReportDao,
    private val fitnessAppointmentRepository: FitnessAppointmentRepository
) {

    @Transactional(readOnly = true)
    fun findAll(): List<ReportResponse> =
        reportDao.findAll().map { it.toResponse() }

    @Transactional(readOnly = true)
    fun findById(id: Long): ReportResponse =
        reportDao.findById(id).orThrow("Report $id not found").toResponse()

    @Transactional(readOnly = true)
    fun findByAppointmentId(appointmentId: Long): ReportResponse =
        reportDao.findByAppointmentId(appointmentId)
            .orThrow("Report for appointment $appointmentId not found")
            .toResponse()

    @Transactional
    fun create(request: ReportRequest): ReportResponse {
        val appointment = fitnessAppointmentRepository.findById(request.appointmentId)
            .orElseThrow { NoSuchElementException("Appointment ${request.appointmentId} not found") }

        check(appointment.status == AppointmentStatusEnum.COMPLETED) {
            "Report can only be created for COMPLETED appointments"
        }

        check(reportDao.findByAppointmentId(request.appointmentId) == null) {
            "Report already exists for appointment ${request.appointmentId}"
        }

        val report = ReportEntity(
            id = 0L,
            fitnessAppointmentEntity = appointment,
            issuedDate = LocalDate.now(),
            diagnosis = request.diagnosis,
            prescription = request.prescription,
            specialistNotes = request.specialistNotes,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        return reportDao.save(report).toResponse()
    }

    @Transactional
    fun update(id: Long, request: ReportRequest): ReportResponse {
        val existing = reportDao.findById(id).orThrow("Report $id not found")
        existing.diagnosis = request.diagnosis
        existing.prescription = request.prescription
        existing.specialistNotes = request.specialistNotes
        existing.updatedAt = LocalDateTime.now()
        return reportDao.save(existing).toResponse()
    }
}