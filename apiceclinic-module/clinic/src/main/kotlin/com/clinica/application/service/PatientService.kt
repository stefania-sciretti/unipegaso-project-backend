package com.clinica.application.service

import com.clinica.application.domain.Patient
import com.clinica.doors.outbound.database.dao.PatientDao
import com.clinica.dto.PatientRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class PatientService(
    private val dao: PatientDao,
) {

    fun getAllPatients(): List<Patient> = dao.getAllPatients()

    fun findById(id: Long): Patient =
        dao.findById(id).orThrow("Patient not found with id: $id")

    fun search(query: String): List<Patient> = dao.search(query.trim())

    @Transactional
    fun create(request: PatientRequest): Patient {
        check(!dao.existsByFiscalCode(request.fiscalCode)) {
            "A patient with fiscal code '${request.fiscalCode}' already exists"
        }
        val patient = Patient(
            id = 0L,
            firstName = request.firstName,
            lastName = request.lastName,
            fiscalCode = request.fiscalCode,
            birthDate = request.birthDate,
            email = request.email,
            phone = request.phone,
            updatedAt = LocalDateTime.now()
        )
        return dao.save(patient)
    }

    @Transactional
    fun update(id: Long, request: PatientRequest): Patient {
        val patient = dao.findById(id).orThrow("Patient not found with id: $id")
        val updated = patient.copy(
            firstName = request.firstName,
            lastName = request.lastName,
            fiscalCode = request.fiscalCode,
            birthDate = request.birthDate,
            email = request.email,
            phone = request.phone,
            updatedAt = LocalDateTime.now()
        )
        return dao.save(updated)
    }

    @Transactional
    fun delete(id: Long) {
        dao.findById(id).orThrow("Patient not found with id: $id")
        dao.deleteById(id)
    }
}