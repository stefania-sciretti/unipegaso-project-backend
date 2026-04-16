package com.clinica.doors.outbound.database.dao

import com.clinica.application.domain.Appointment
import com.clinica.doors.outbound.database.entities.AppointmentEntity
import com.clinica.doors.outbound.database.entities.DoctorEntity
import com.clinica.doors.outbound.database.entities.PatientEntity
import com.clinica.doors.outbound.database.mappers.toDomain
import com.clinica.doors.outbound.database.mappers.toEntity
import com.clinica.doors.outbound.database.repositories.AppointmentRepository
import com.clinica.doors.outbound.database.repositories.DoctorRepository
import com.clinica.doors.outbound.database.repositories.PatientRepository
import org.springframework.stereotype.Component

@Component
class AppointmentDao(
    private val appointmentRepository: AppointmentRepository,
    private val patientRepository: PatientRepository,
    private val doctorRepository: DoctorRepository
) {

    fun findAll(
        patientId: Long?,
        doctorId: Long?,
        status: String?
    ): List<Appointment> =
        appointmentRepository.search(patientId, doctorId, status)
            .map { it.toDomain() }

    fun findById(id: Long): Appointment? =
        appointmentRepository.findById(id).orElse(null)?.toDomain()

    fun existsById(id: Long): Boolean =
        appointmentRepository.existsById(id)

    fun save(appointment: Appointment): Appointment {
        val patientId = appointment.patient.id
        val doctorId = appointment.doctor.id

        val patientEntity: PatientEntity = patientRepository.findById(patientId)
            .orElseThrow { IllegalArgumentException("Patient not found with id: $patientId") }

        val doctorEntity: DoctorEntity = doctorRepository.findById(doctorId)
            .orElseThrow { IllegalArgumentException("Doctor not found with id: $doctorId") }

        val existing: AppointmentEntity? =
            if (appointment.id != 0L) {
                appointmentRepository.findById(appointment.id).orElse(null)
            } else {
                null
            }

        val entityToSave = appointment.toEntity(
            patientEntityProvider = { patientEntity },
            doctorEntityProvider = { doctorEntity },
            existingEntity = existing
        )

        val saved = appointmentRepository.save(entityToSave)
        return saved.toDomain()
    }

    fun deleteById(id: Long) =
        appointmentRepository.deleteById(id)
}