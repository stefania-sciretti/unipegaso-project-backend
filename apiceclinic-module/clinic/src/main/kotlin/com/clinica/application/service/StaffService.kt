package com.clinica.application.service

import com.clinica.application.domain.Specialist
import com.clinica.doors.outbound.database.dao.SpecialistDao
import com.clinica.dto.SpecialistResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SpecialistService(
    private val specialistDao: SpecialistDao
) {

    @Transactional(readOnly = true)
    fun findAll(): List<SpecialistResponse> =
        specialistDao.findAll().map { it.toResponse() }

    @Transactional(readOnly = true)
    fun findByRole(role: String): List<SpecialistResponse> =
        specialistDao.findByRole(role).map { it.toResponse() }

    @Transactional(readOnly = true)
    fun findById(id: Long): SpecialistResponse =
        specialistDao.findById(id).orThrow("Specialist member $id not found").toResponse()

    private fun Specialist.toResponse(): SpecialistResponse =
        SpecialistResponse(
            id = id,
            firstName = firstName,
            lastName = lastName,
            role = role,
            bio = bio,
            email = email,
            createdAt = createdAt
        )
}