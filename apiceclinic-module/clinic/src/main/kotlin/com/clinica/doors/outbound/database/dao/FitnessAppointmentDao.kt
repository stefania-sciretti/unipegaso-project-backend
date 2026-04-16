package com.clinica.doors.outbound.database.dao

import com.clinica.application.domain.FitnessAppointment
import com.clinica.application.domain.Patient
import com.clinica.application.domain.Staff
import com.clinica.doors.outbound.database.entities.FitnessAppointmentEntity
import com.clinica.doors.outbound.database.entities.PatientEntity
import com.clinica.doors.outbound.database.entities.StaffEntity
import com.clinica.doors.outbound.database.repositories.FitnessAppointmentRepository
import com.clinica.doors.outbound.database.repositories.PatientRepository
import com.clinica.doors.outbound.database.repositories.StaffRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class FitnessAppointmentDao(
    private val repository: FitnessAppointmentRepository,
    private val patientRepository: PatientRepository,
    private val staffRepository: StaffRepository
) {

    @Transactional(readOnly = true)
    fun findAll(clientId: Long?, trainerId: Long?, status: String?): List<FitnessAppointment> =
        repository.search(clientId, trainerId, status).map { it.toDomain() }

    @Transactional(readOnly = true)
    fun findById(id: Long): FitnessAppointment? =
        repository.findById(id).orElse(null)?.toDomain()

    @Transactional
    fun save(appointment: FitnessAppointment): FitnessAppointment {
        val clientId = appointment.client.id
        val staffId = appointment.staff.id

        val clientEntity = patientRepository.findById(clientId)
            .orElseThrow { IllegalArgumentException("Client not found: $clientId") }
        val staffEntity = staffRepository.findById(staffId)
            .orElseThrow { IllegalArgumentException("Staff not found: $staffId") }

        val existing = if (appointment.id != 0L)
            repository.findById(appointment.id).orElse(null) else null

        val entity = existing?.also {
            it.clientEntity = clientEntity
            it.staff = staffEntity
            it.scheduledAt = appointment.scheduledAt
            it.serviceType = appointment.serviceType
            it.status = appointment.status
            it.notes = appointment.notes
        } ?: FitnessAppointmentEntity(
            id = appointment.id,
            clientEntity = clientEntity,
            staff = staffEntity,
            scheduledAt = appointment.scheduledAt,
            serviceType = appointment.serviceType,
            status = appointment.status,
            notes = appointment.notes
        )

        return repository.save(entity).toDomain()
    }

    @Transactional
    fun deleteById(id: Long) = repository.deleteById(id)

    private fun FitnessAppointmentEntity.toDomain(): FitnessAppointment =
        FitnessAppointment(
            id = id,
            client = clientEntity.clientToDomain(),
            staff = staff.staffToDomain(),
            scheduledAt = scheduledAt,
            serviceType = serviceType,
            status = status,
            notes = notes,
            updatedAt = updatedAt
        )

    private fun PatientEntity.clientToDomain(): Patient =
        Patient(
            id = id,
            firstName = firstName,
            lastName = lastName,
            fiscalCode = fiscalCode,
            birthDate = birthDate,
            email = email,
            phone = phone,
            updatedAt = updatedAt
        )

    private fun StaffEntity.staffToDomain(): Staff =
        Staff(
            id = id,
            firstName = firstName,
            lastName = lastName,
            role = role,
            bio = bio,
            email = email,
            updatedAt = updatedAt
        )
}