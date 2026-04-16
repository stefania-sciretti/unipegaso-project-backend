package com.clinica.service

import com.clinica.domain.PatientEntity
import com.clinica.dto.PatientRequest
import com.clinica.dto.PatientResponse
import com.clinica.repository.PatientRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PatientService(private val patientRepository: PatientRepository) {

    @Transactional(readOnly = true)
    fun findAll(): List<PatientResponse> =
        patientRepository.findAll().map { it.toResponse() }

    @Transactional(readOnly = true)
    fun findById(id: Long): PatientResponse =
        patientRepository.findById(id)?.toResponse() ?: throw NoSuchElementException("Patient not found with id: $id")

    @Transactional(readOnly = true)
    fun search(query: String): List<PatientResponse> =
        patientRepository
            .findByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCase(query, query)
            .map { it.toResponse() }

    fun create(request: PatientRequest): PatientResponse {
        require(!patientRepository.existsByFiscalCode(request.fiscalCode)) {
            "A patient with fiscal code '${request.fiscalCode}' already exists"
        }
        val patientEntity = PatientEntity(
            firstName = request.firstName,
            lastName = request.lastName,
            fiscalCode = request.fiscalCode,
            birthDate = request.birthDate,
            email = request.email,
            phone = request.phone
        )
        return patientRepository.save(patientEntity).toResponse()
    }

    fun update(id: Long, request: PatientRequest): PatientResponse {
        val patient = patientRepository.findById(id) ?: throw NoSuchElementException("Patient not found with id: $id")

        require(
            patient.fiscalCode == request.fiscalCode ||
            !patientRepository.existsByFiscalCode(request.fiscalCode)
        ) {
            "A patient with fiscal code '${request.fiscalCode}' already exists"
        }

        patient.firstName = request.firstName
        patient.lastName = request.lastName
        patient.fiscalCode = request.fiscalCode
        patient.birthDate = request.birthDate
        patient.email = request.email
        patient.phone = request.phone

        return patientRepository.save(patient).toResponse()
    }

    fun delete(id: Long) {
        if (!patientRepository.existsById(id)) {
            throw NoSuchElementException("Patient not found with id: $id")
        }
        patientRepository.deleteById(id)
    }

    private fun PatientEntity.toResponse() = PatientResponse(
        id = id,
        firstName = firstName,
        lastName = lastName,
        fiscalCode = fiscalCode,
        birthDate = birthDate,
        email = email,
        phone = phone,
        createdAt = createdAt
    )
}
