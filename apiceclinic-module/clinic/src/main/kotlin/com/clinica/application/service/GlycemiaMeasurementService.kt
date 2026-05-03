package com.clinica.application.service

import com.clinica.application.domain.GlycemiaContext
import com.clinica.application.domain.GlycemiaMeasurement
import com.clinica.doors.outbound.database.dao.GlycemiaMeasurementDao
import com.clinica.doors.outbound.database.dao.PatientDao
import com.clinica.doors.outbound.database.dao.SpecialistDao
import com.clinic.model.GlycemiaMeasurementRequest
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
    fun findAll(patientId: Long?): List<GlycemiaMeasurement> =
        glycemiaMeasurementDao.findAll(patientId)

    @Transactional(readOnly = true)
    fun findById(id: Long): GlycemiaMeasurement =
        glycemiaMeasurementDao.findById(id)
            .orThrow("Glycemia measurement not found with id: $id")

    @Transactional
    fun create(request: GlycemiaMeasurementRequest): GlycemiaMeasurement {
        val patient = patientDao.findById(request.patientId)
            .orThrow("Patient not found with id: ${request.patientId}")

        val specialist = specialistDao.findById(request.specialistId)
            .orThrow("Specialist not found with id: ${request.specialistId}")

        val context = GlycemiaContext.valueOf(request.context.value)
        val now = LocalDateTime.now()

        val measurement = GlycemiaMeasurement(
            patient = patient,
            specialist = specialist,
            measuredAt = request.measuredAt.toLocalDateTime(),
            valueMgDl = request.valueMgDl,
            context = context,
            notes = request.notes,
            createdAt = now,
            updatedAt = now
        )
        return glycemiaMeasurementDao.save(measurement)
    }

    @Transactional
    fun update(id: Long, request: GlycemiaMeasurementRequest): GlycemiaMeasurement {
        val existing = glycemiaMeasurementDao.findById(id)
            .orThrow("Glycemia measurement not found with id: $id")

        val patient = patientDao.findById(request.patientId)
            .orThrow("Patient not found with id: ${request.patientId}")

        val specialist = specialistDao.findById(request.specialistId)
            .orThrow("Specialist not found with id: ${request.specialistId}")

        val context = GlycemiaContext.valueOf(request.context.value)

        val updated = existing.copy(
            patient = patient,
            specialist = specialist,
            measuredAt = request.measuredAt.toLocalDateTime(),
            valueMgDl = request.valueMgDl,
            context = context,
            notes = request.notes,
            updatedAt = LocalDateTime.now()
        )
        return glycemiaMeasurementDao.save(updated)
    }

    @Transactional
    fun delete(id: Long) {
        glycemiaMeasurementDao.findById(id)
            .orThrow("Glycemia measurement not found with id: $id")
        glycemiaMeasurementDao.deleteById(id)
    }

}
