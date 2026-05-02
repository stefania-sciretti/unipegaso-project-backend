package com.clinica.doors.outbound.database.dao

import com.clinica.application.domain.Appointment
import com.clinica.application.domain.AppointmentStatusEnum
import com.clinica.doors.outbound.database.entities.AppointmentEntity
import com.clinica.doors.outbound.database.entities.PatientEntity
import com.clinica.doors.outbound.database.entities.SpecialistEntity
import com.clinica.doors.outbound.database.mappers.toDomain
import com.clinica.doors.outbound.database.mappers.toEntity
import com.clinica.doors.outbound.database.repositories.AppointmentRepository
import com.clinica.doors.outbound.database.repositories.PatientRepository
import com.clinica.doors.outbound.database.repositories.SpecialistRepository
import org.springframework.stereotype.Component

@Component
class AppointmentDao(
    private val appointmentRepository: AppointmentRepository,
    private val patientRepository: PatientRepository,
    private val specialistRepository: SpecialistRepository
) {

    fun findAll(
        patientId: Long?,
        specialistId: Long?,
        status: AppointmentStatusEnum?
    ): List<Appointment> =
        appointmentRepository.search(patientId, specialistId, status?.name)
            .map { it.toDomain() }

    fun findById(id: Long): Appointment? =
        appointmentRepository.findById(id).orElse(null)?.toDomain()

    fun save(appointment: Appointment): Appointment {
        val patientId = appointment.patient.id
        val specialistId = appointment.specialist.id

        val patientEntity: PatientEntity = patientRepository.findById(patientId)
            .orElseThrow { IllegalArgumentException("Patient not found with id: $patientId") }

        val specialistEntity: SpecialistEntity = specialistRepository.findById(specialistId)
            .orElseThrow { IllegalArgumentException("Specialist not found with id: $specialistId") }

        val existing: AppointmentEntity? =
            if (appointment.id != 0L) {
                appointmentRepository.findById(appointment.id).orElse(null)
            } else {
                null
            }

        val entityToSave = appointment.toEntity(
            patientEntityProvider = { patientEntity },
            specialistEntityProvider = { specialistEntity },
            existingEntity = existing
        )

        val saved = appointmentRepository.save(entityToSave)
        return saved.toDomain()
    }

    fun deleteById(id: Long) =
        appointmentRepository.deleteById(id)
}