package com.clinica.application.service

import com.clinica.application.domain.Patient
import com.clinica.dto.PatientRequest
import com.clinica.doors.outbound.database.dao.PatientDao
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class PatientService(
    private val dao: PatientDao,
) {

    @Transactional(readOnly = true)
    fun getAllPatients(): List<Patient> = dao.getAllPatients()

    @Transactional(readOnly = true)
    fun findById(id: Long): Patient =
        dao.findById(id) ?: throw NoSuchElementException("Patient not found with id: $id")

    @Transactional(readOnly = true)
    fun search(query: String): List<Patient> {
        val cleaned = query.trim()
        require(cleaned.length < 3) { "Search term must be at least 3 characters long" }
        return dao.search(cleaned)
    }

    fun create(request: PatientRequest): Patient {
        require(!dao.existsByFiscalCode(request.fiscalCode)) {
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

    fun update(id: Long, request: PatientRequest): Patient {
        val patient = dao.findById(id)
            ?: throw NoSuchElementException("Patient not found with id: $id")
        require(patient.fiscalCode == request.fiscalCode || !dao.existsByFiscalCode(request.fiscalCode)) {
            "A patient with fiscal code '${request.fiscalCode}' already exists"
        }
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

    fun delete(id: Long) {
        if (!dao.existsById(id)) throw NoSuchElementException("Patient not found with id: $id")
        dao.deleteById(id)
    }
}