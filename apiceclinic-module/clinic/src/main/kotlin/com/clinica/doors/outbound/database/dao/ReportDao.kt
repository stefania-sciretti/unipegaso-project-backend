package com.clinica.doors.outbound.database.dao

import com.clinica.application.domain.Report
import com.clinica.doors.outbound.database.entities.ReportEntity
import com.clinica.doors.outbound.database.mappers.toDomain
import com.clinica.doors.outbound.database.repositories.FitnessAppointmentRepository
import com.clinica.doors.outbound.database.repositories.ReportRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ReportDao(
    private val reportRepository: ReportRepository,
    private val fitnessAppointmentRepository: FitnessAppointmentRepository
) {

    fun findAll(): List<Report> =
        reportRepository.findAll().map { it.toDomain() }

    fun findById(id: Long): Report? =
        reportRepository.findById(id).orElse(null)?.toDomain()

    fun findByAppointmentId(appointmentId: Long): Report? =
        reportRepository.findByFitnessAppointmentEntityId(appointmentId)?.toDomain()

    @Transactional
    fun save(report: Report): Report {
        val fitnessApptEntity = fitnessAppointmentRepository.findById(report.appointment.id)
            .orElseThrow { IllegalArgumentException("Appointment not found: ${report.appointment.id}") }

        val entity = if (report.id == 0L) {
            ReportEntity(
                fitnessAppointmentEntity = fitnessApptEntity,
                issuedDate = report.issuedDate,
                diagnosis = report.diagnosis,
                prescription = report.prescription,
                specialistNotes = report.specialistNotes,
                createdAt = report.createdAt,
                updatedAt = report.updatedAt
            )
        } else {
            reportRepository.findById(report.id)
                .orElseThrow { IllegalArgumentException("Report not found: ${report.id}") }
                .also {
                    it.diagnosis = report.diagnosis
                    it.prescription = report.prescription
                    it.specialistNotes = report.specialistNotes
                    it.updatedAt = report.updatedAt
                }
        }
        return reportRepository.save(entity).toDomain()
    }

    @Transactional
    fun deleteById(id: Long) =
        reportRepository.deleteById(id)
}