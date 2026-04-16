package com.clinica.service

import com.clinica.domain.Doctor
import com.clinica.dto.DoctorRequest
import com.clinica.dto.DoctorResponse
import com.clinica.repository.DoctorRepository
import com.clinica.util.orEntityNotFound
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DoctorService(private val doctorRepository: DoctorRepository) {

    @Transactional(readOnly = true)
    fun findAll(specialization: String?): List<DoctorResponse> {
        val doctors = if (!specialization.isNullOrBlank()) {
            doctorRepository.findBySpecializationContainingIgnoreCase(specialization)
        } else {
            doctorRepository.findAll()
        }
        return doctors.map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): DoctorResponse =
        doctorRepository.findById(id).orEntityNotFound("Doctor", id).toResponse()

    fun create(request: DoctorRequest): DoctorResponse {
        require(!doctorRepository.existsByLicenseNumber(request.licenseNumber)) {
            "A doctor with license number '${request.licenseNumber}' already exists"
        }
        val doctor = Doctor(
            firstName = request.firstName,
            lastName = request.lastName,
            specialization = request.specialization,
            email = request.email,
            licenseNumber = request.licenseNumber
        )
        return doctorRepository.save(doctor).toResponse()
    }

    fun update(id: Long, request: DoctorRequest): DoctorResponse {
        val doctor = doctorRepository.findById(id).orEntityNotFound("Doctor", id)

        require(
            doctor.licenseNumber == request.licenseNumber ||
            !doctorRepository.existsByLicenseNumber(request.licenseNumber)
        ) {
            "A doctor with license number '${request.licenseNumber}' already exists"
        }

        doctor.firstName = request.firstName
        doctor.lastName = request.lastName
        doctor.specialization = request.specialization
        doctor.email = request.email
        doctor.licenseNumber = request.licenseNumber

        return doctorRepository.save(doctor).toResponse()
    }

    fun        val doctor = doctorRepository.findById(id).orEntityNotFound("Doctor", id)
        doctorRepository.delete(doctor)n("Doctor not found with id: $id")
        }
        doctorRepository.deleteById(id)
    }

    private fun Doctor.toResponse() = DoctorResponse(
        id = id,
        firstName = firstName,
        lastName = lastName,
        specialization = specialization,
        email = email,
        licenseNumber = licenseNumber,
        createdAt = createdAt
    )
}
