package com.clinica.repository

import com.clinica.domain.PatientEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PatientRepository : JpaRepository<PatientEntity, Long> {
    fun findByFiscalCode(fiscalCode: String): PatientEntity?
    fun existsByFiscalCode(fiscalCode: String): Boolean
    fun findByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCase(
        lastName: String, firstName: String
    ): List<PatientEntity>
}
