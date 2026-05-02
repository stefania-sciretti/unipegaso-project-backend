package com.clinica.doors.outbound.database.dao

import com.clinica.application.domain.GlycemiaMeasurement
import com.clinica.doors.outbound.database.entities.GlycemiaMeasurementEntity
import com.clinica.doors.outbound.database.entities.PatientEntity
import com.clinica.doors.outbound.database.entities.SpecialistEntity
import com.clinica.doors.outbound.database.mappers.toDomain
import com.clinica.doors.outbound.database.mappers.toEntity
import com.clinica.doors.outbound.database.repositories.GlycemiaMeasurementRepository
import com.clinica.doors.outbound.database.repositories.PatientRepository
import com.clinica.doors.outbound.database.repositories.SpecialistRepository
import org.springframework.stereotype.Component

@Component
class GlycemiaMeasurementDao(
    private val glycemiaMeasurementRepository: GlycemiaMeasurementRepository,
    private val patientRepository: PatientRepository,
    private val specialistRepository: SpecialistRepository
) {

    fun findAll(patientId: Long?): List<GlycemiaMeasurement> =
        if (patientId == null) {
            glycemiaMeasurementRepository.findAllByOrderByMeasuredAtDesc().map { it.toDomain() }
        } else {
            glycemiaMeasurementRepository.findByPatientEntityIdOrderByMeasuredAtDesc(patientId).map { it.toDomain() }
        }

    fun findById(id: Long): GlycemiaMeasurement? =
        glycemiaMeasurementRepository.findById(id).orElse(null)?.toDomain()

    fun save(measurement: GlycemiaMeasurement): GlycemiaMeasurement {
        val patientEntity: PatientEntity = patientRepository.findById(measurement.patient.id)
            .orElseThrow { IllegalArgumentException("Patient not found with id: ${measurement.patient.id}") }

        val specialistEntity: SpecialistEntity = specialistRepository.findById(measurement.specialist.id)
            .orElseThrow { IllegalArgumentException("Specialist not found with id: ${measurement.specialist.id}") }

        val existing: GlycemiaMeasurementEntity? =
            if (measurement.id != 0L) glycemiaMeasurementRepository.findById(measurement.id).orElse(null)
            else null

        val entityToSave = measurement.toEntity(
            patientEntityProvider = { patientEntity },
            specialistEntityProvider = { specialistEntity },
            existingEntity = existing
        )
        return glycemiaMeasurementRepository.save(entityToSave).toDomain()
    }

    fun deleteById(id: Long) = glycemiaMeasurementRepository.deleteById(id)
}
