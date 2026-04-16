package com.clinica.doors.outbound.database.dao

import com.clinica.application.domain.Patient
import com.clinica.doors.outbound.database.mappers.toDomain
import com.clinica.doors.outbound.database.mappers.toEntity
import com.clinica.doors.outbound.database.repositories.PatientRepository
import org.springframework.stereotype.Component

@Component
class PatientDao(
    private val repository: PatientRepository
) {

    fun getAllPatients(): List<Patient> =
        repository.findAll().map { it.toDomain() }

    fun findById(id: Long): Patient? =
        repository.findById(id).orElse(null)?.toDomain()

    fun search(term: String): List<Patient> =
        repository.searchByNameOrLastName(term).map { it.toDomain() }

    fun existsByFiscalCode(fiscalCode: String): Boolean =
        repository.existsByFiscalCode(fiscalCode)

    fun existsById(id: Long): Boolean =
        repository.existsById(id)

    fun save(patient: Patient): Patient {
        val entity = patient.toEntity()
        val saved = repository.save(entity)
        return saved.toDomain()
    }

    fun deleteById(id: Long) =
        repository.deleteById(id)
}