package com.clinica.doors.outbound.database.dao

import com.clinica.doors.outbound.database.entities.ReportEntity
import com.clinica.doors.outbound.database.repositories.ReportRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ReportDao(
    private val reportRepository: ReportRepository
) {

    fun findAll(): List<ReportEntity> =
        reportRepository.findAll()

    fun findById(id: Long): ReportEntity? =
        reportRepository.findById(id).orElse(null)

    fun findByAppointmentId(appointmentId: Long): ReportEntity? =
        reportRepository.findByFitnessAppointmentEntityId(appointmentId)

    @Transactional
    fun save(report: ReportEntity): ReportEntity =
        reportRepository.save(report)

    @Transactional
    fun deleteById(id: Long) =
        reportRepository.deleteById(id)
}