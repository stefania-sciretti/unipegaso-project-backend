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
        val patientEntity: PatientEntity = patientRepository.findById(appointment.patient.id)
            .orElseThrow { IllegalArgumentException("Patient not found with id: ${appointment.patient.id}") }

        val specialistEntity: SpecialistEntity = specialistRepository.findById(appointment.specialist.id)
            .orElseThrow { IllegalArgumentException("Specialist not found with id: ${appointment.specialist.id}") }

        val existing: AppointmentEntity? =
            if (appointment.id != 0L) appointmentRepository.findById(appointment.id).orElse(null)
            else null

        val entityToSave = appointment.toEntity(
            patientEntityProvider = { patientEntity },
            specialistEntityProvider = { specialistEntity },
            areaEntityProvider = { specialistEntity.area },
            existingEntity = existing
        )

        return appointmentRepository.save(entityToSave).toDomain()
    }
}
