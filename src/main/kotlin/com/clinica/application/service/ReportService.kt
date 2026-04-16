package com.clinica.service

import com.clinica.domain.AppointmentStatus
import com.clinica.domain.Report
import com.clinica.dto.ReportRequest
import com.clinica.dto.ReportResponse
import com.clinica.repository.AppointmentRepository
import com.clinica.repository.ReportRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReportService(
    private val reportRepository: ReportRepository,
    private val appointmentRepository: AppointmentRepository
) {

    @Transactional(readOnly = true)
    fun findAll(): List<ReportResponse> =
        reportRepository.findAll().map { it.toResponse() }

    @Transactional(readOnly = true)
    fun findById(id: Long): ReportResponse =
        reportRepository.findById(id)
            .orElseThrow { NoSuchElementException("Report not found with id: $id") }
            .toResponse()

    @Transactional(readOnly = true)
    fun findByAppointmentId(appointmentId: Long): ReportResponse =
        reportRepository.findByAppointmentId(appointmentId)
            .orElseThrow { NoSuchElementException("Report not found for appointment id: $appointmentId") }
            .toResponse()

    fun create(request: ReportRequest): ReportResponse {
        val appointment = appointmentRepository.findById(request.appointmentId)
            .orElseThrow { NoSuchElementException("Appointment not found with id: ${request.appointmentId}") }

        check(appointment.status == AppointmentStatus.COMPLETED) {
            "Cannot create a report for an appointment that is not COMPLETED"
        }
        check(!reportRepository.existsByAppointmentId(request.appointmentId)) {
            "A report already exists for appointment id: ${request.appointmentId}"
        }

        val report = Report(
            appointmentEntity = appointment,
            diagnosis = request.diagnosis,
            prescription = request.prescription,
            doctorNotes = request.doctorNotes
        )
        return reportRepository.save(report).toResponse()
    }

    fun update(id: Long, request: ReportRequest): ReportResponse {
        val report = reportRepository.findById(id)
            .orElseThrow { NoSuchElementException("Report not found with id: $id") }

        report.diagnosis = request.diagnosis
        report.prescription = request.prescription
        report.doctorNotes = request.doctorNotes

        return reportRepository.save(report).toResponse()
    }

    private fun Report.toResponse() = ReportResponse(
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
