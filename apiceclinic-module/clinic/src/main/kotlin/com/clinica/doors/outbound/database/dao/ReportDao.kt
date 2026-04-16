package com.clinica.doors.outbound.database.dao

import com.clinica.doors.outbound.database.entities.ReportEntity
import com.clinica.doors.outbound.database.repositories.ReportRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ReportDao(
    private val reportRepository: ReportRepository
) {

    @Transactional(readOnly = true)
    fun findAll(): List<ReportEntity> =
        reportRepository.findAll()

    @Transactional(readOnly = true)
    fun findById(id: Long): ReportEntity? =
        reportRepository.findById(id).orElse(null)

    @Transactional(readOnly = true)
    fun findByAppointmentId(appointmentId: Long): ReportEntity? =
        reportRepository.findByAppointmentEntityId(appointmentId)

    @Transactional
    fun save(report: ReportEntity): ReportEntity =
        reportRepository.save(report)

    @Transactional
    fun deleteById(id: Long) =
        reportRepository.deleteById(id)
}