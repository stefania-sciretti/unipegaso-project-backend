package com.clinica.doors.outbound.database.dao

import com.clinica.application.domain.AppointmentStatusEnum
import com.clinica.application.domain.FitnessAppointment
import com.clinica.doors.outbound.database.entities.FitnessAppointmentEntity
import com.clinica.doors.outbound.database.mappers.toDomain
import com.clinica.doors.outbound.database.repositories.FitnessAppointmentRepository
import com.clinica.doors.outbound.database.repositories.PatientRepository
import com.clinica.doors.outbound.database.repositories.SpecialistRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class FitnessAppointmentDao(
    private val repository: FitnessAppointmentRepository,
    private val patientRepository: PatientRepository,
    private val specialistRepository: SpecialistRepository
) {

    @Transactional(readOnly = true)
    fun findAll(patientId: Long?, specialistId: Long?, status: String?): List<FitnessAppointment> {
        val statusEnum = status?.let { AppointmentStatusEnum.parse(it) }
        return repository.search(patientId, specialistId, statusEnum).map { it.toDomain() }
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): FitnessAppointment? =
        repository.findById(id).orElse(null)?.toDomain()

    @Transactional
    fun save(appointment: FitnessAppointment): FitnessAppointment {
        val patientEntity = patientRepository.findById(appointment.patient.id)
            .orElseThrow { IllegalArgumentException("Patient not found: ${appointment.patient.id}") }
        val specialistEntity = specialistRepository.findById(appointment.specialist.id)
            .orElseThrow { IllegalArgumentException("Specialist not found: ${appointment.specialist.id}") }

        val existing = if (appointment.id != 0L)
            repository.findById(appointment.id).orElse(null) else null

        val entity = existing?.also {
            it.patientEntity = patientEntity
            it.specialist = specialistEntity
            it.scheduledAt = appointment.scheduledAt
            it.serviceType = appointment.serviceType
            it.status = appointment.status
            it.notes = appointment.notes
        } ?: FitnessAppointmentEntity(
            id = if (appointment.id == 0L) null else appointment.id,
            patientEntity = patientEntity,
            specialist = specialistEntity,
            scheduledAt = appointment.scheduledAt,
            serviceType = appointment.serviceType,
            status = appointment.status,
            notes = appointment.notes
        )

        return repository.save(entity).toDomain()
    }

    @Transactional
    fun deleteById(id: Long) = repository.deleteById(id)
}