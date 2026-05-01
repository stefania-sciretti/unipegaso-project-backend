package com.clinica.doors.outbound.database.dao

import com.clinica.application.domain.Specialist
import com.clinica.doors.outbound.database.mappers.toDomain
import com.clinica.doors.outbound.database.repositories.SpecialistRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class SpecialistDao(
    private val specialistRepository: SpecialistRepository
) {

    @Transactional(readOnly = true)
    fun findAll(): List<Specialist> =
        specialistRepository.findAll().map { it.toDomain() }

    @Transactional(readOnly = true)
    fun findByRole(role: String): List<Specialist> =
        specialistRepository.findByRole(role).map { it.toDomain() }

    @Transactional(readOnly = true)
    fun findById(id: Long): Specialist? =
        specialistRepository.findById(id).orElse(null)?.toDomain()
}