package com.clinica.doors.outbound.database.repositories

import com.clinica.doors.outbound.database.entities.PatientEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PatientRepository : JpaRepository<PatientEntity, Long> {

    @Query(
        """
        SELECT p FROM PatientEntity p
        WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :term, '%'))
           OR LOWER(p.lastName)  LIKE LOWER(CONCAT('%', :term, '%'))
        """
    )
    fun searchByNameOrLastName(@Param("term") term: String): List<PatientEntity>

    fun existsByFiscalCode(fiscalCode: String): Boolean
}
