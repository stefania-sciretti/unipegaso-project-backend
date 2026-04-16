package com.clinica.repository

import com.clinica.domain.Doctor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DoctorRepository : JpaRepository<Doctor, Long> {
    fun findByLicenseNumber(licenseNumber: String): Doctor?
    fun existsByLicenseNumber(licenseNumber: String): Boolean
    fun findBySpecializationContainingIgnoreCase(specialization: String): List<Doctor>
}
