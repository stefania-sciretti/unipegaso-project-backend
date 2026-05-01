package com.clinica.doors.outbound.database.repositories

import com.clinica.doors.outbound.database.entities.SpecialistEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SpecialistRepository : JpaRepository<SpecialistEntity, Long> {

    fun findByRole(role: String): List<SpecialistEntity>
}