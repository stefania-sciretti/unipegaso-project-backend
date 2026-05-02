package com.clinica.application.service

import com.clinic.model.GlycemiaMeasurementResponse
import com.clinica.application.domain.GlycemiaContext
import com.clinica.application.domain.GlycemiaMeasurement
import com.clinica.application.mappers.toResponse
import com.clinica.doors.outbound.database.dao.GlycemiaMeasurementDao
import com.clinica.doors.outbound.database.dao.PatientDao
import com.clinica.doors.outbound.database.dao.SpecialistDao
import com.clinica.dto.GlycemiaMeasurementRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class GlycemiaMeasurementService(
    private val glycemiaMeasurementDao: GlycemiaMeasurementDao,
    private val patientDao: PatientDao,
    private val specialistDao: SpecialistDao
) {

    @Transactional(readOnly = true)
    fun findAll(patientId: Long?): List<GlycemiaMeasurementResponse> =
        glycemiaMeasurementDao.findAll(patientId).map { it.toResponse() }

    @Transactional(readOnly = true)
    fun findById(id: Long): GlycemiaMeasurementResponse =
        glycemiaMeasurementDao.findById(id)
            .orThrow("Glycemia measurement not found with id: $id")
            .toResponse()

    @Transactional
    fun create(request: GlycemiaMeasurementRequest): GlycemiaMeasurementResponse {
        val patient = patientDao.findById(request.patientId)
            .orThrow("Patient not found with id: ${request.patientId}")

        val specialist = specialistDao.findById(request.specialistId)
            .orThrow("Specialist not found with id: ${request.specialistId}")

        val context = parseContext(request.context)
        val now = LocalDateTime.now()

        val measurement = GlycemiaMeasurement(
            patient = patient,
            specialist = specialist,
            measuredAt = request.measuredAt,
            valueMgDl = request.valueMgDl,
            context = context,
            notes = request.notes,
            createdAt = now,
            updatedAt = now
        )
        return glycemiaMeasurementDao.save(measurement).toResponse()
    }

    @Transactional
    fun update(id: Long, request: GlycemiaMeasurementRequest): GlycemiaMeasurementResponse {
        val existing = glycemiaMeasurementDao.findById(id)
            .orThrow("Glycemia measurement not found with id: $id")

        val patient = patientDao.findById(request.patientId)
            .orThrow("Patient not found with id: ${request.patientId}")

        val specialist = specialistDao.findById(request.specialistId)
            .orThrow("Specialist not found with id: ${request.specialistId}")

        val context = parseContext(request.context)

        val updated = existing.copy(
            patient = patient,
            specialist = specialist,
            measuredAt = request.measuredAt,
            valueMgDl = request.valueMgDl,
            context = context,
            notes = request.notes,
            updatedAt = LocalDateTime.now()
        )
        return glycemiaMeasurementDao.save(updated).toResponse()
    }

    @Transactional
    fun delete(id: Long) {
        glycemiaMeasurementDao.findById(id)
            .orThrow("Glycemia measurement not found with id: $id")
        glycemiaMeasurementDao.deleteById(id)
    }

    private fun parseContext(value: String): GlycemiaContext =
        runCatching { GlycemiaContext.valueOf(value) }
            .getOrElse { throw IllegalArgumentException("Invalid context: '$value'. Valid values: ${GlycemiaContext.entries.joinToString()}") }

}
