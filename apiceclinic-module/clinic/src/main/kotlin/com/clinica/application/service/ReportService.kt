package com.clinica.application.service

import com.clinica.application.domain.AppointmentStatus
import com.clinica.doors.outbound.database.dao.ReportDao
import com.clinica.doors.outbound.database.entities.ReportEntity
import com.clinica.doors.outbound.database.repositories.AppointmentRepository
import com.clinica.dto.ReportRequest
import com.clinica.dto.ReportResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class ReportService(
    private val reportDao: ReportDao,
    private val appointmentRepository: AppointmentRepository
) {

    @Transactional(readOnly = true)
    fun findAll(): List<ReportResponse> =
        reportDao.findAll().map { it.toResponse() }

    @Transactional(readOnly = true)
    fun findById(id: Long): ReportResponse {
        val report = reportDao.findById(id) ?: throw NoSuchElementException("Report $id not found")
        return report.toResponse()
    }

    @Transactional(readOnly = true)
    fun findByAppointmentId(appointmentId: Long): ReportResponse {
        val report = reportDao.findByAppointmentId(appointmentId)
            ?: throw NoSuchElementException("Report for appointment $appointmentId not found")
        return report.toResponse()
    }

    @Transactional
    fun create(request: ReportRequest): ReportResponse {
        val appointment = appointmentRepository.findById(request.appointmentId)
            .orElseThrow { NoSuchElementException("Appointment ${request.appointmentId} not found") }

        check(appointment.status != AppointmentStatus.COMPLETED.name) {
            "Report can only be created for COMPLETED appointments"
        }

        check(reportDao.findByAppointmentId(request.appointmentId) != null) {
            "Report already exists for appointment ${request.appointmentId}"
        }

        val report = ReportEntity(
            id = 0,
            appointmentEntity = appointment,
            issuedDate = LocalDate.now(),
            diagnosis = request.diagnosis,
            prescription = request.prescription,
            doctorNotes = request.doctorNotes,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        return reportDao.save(report).toResponse()
    }

    @Transactional
    fun update(id: Long, request: ReportRequest): ReportResponse {
        val existing = reportDao.findById(id) ?: throw NoSuchElementException("Report $id not found")
        existing.diagnosis = request.diagnosis
        existing.prescription = request.prescription
        existing.doctorNotes = request.doctorNotes
        existing.updatedAt = LocalDateTime.now()
        return reportDao.save(existing).toResponse()
    }

    private fun ReportEntity.toResponse(): ReportResponse =
        ReportResponse(
            id = id,
            appointmentId = appointmentEntity.id,
            patientFullName = "${appointmentEntity.patientEntity.firstName} ${appointmentEntity.patientEntity.lastName}",
            doctorFullName = "${appointmentEntity.doctor.firstName} ${appointmentEntity.doctor.lastName}",
            visitType = appointmentEntity.visitType,
            scheduledAt = appointmentEntity.scheduledAt,
            issuedDate = issuedDate,
            diagnosis = diagnosis,
            prescription = prescription,
            doctorNotes = doctorNotes,
            createdAt = createdAt
        )
}