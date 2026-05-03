package com.clinica.application.service

import com.clinic.model.SpecialistResponse
import com.clinica.application.domain.Specialist
import com.clinica.application.mappers.toResponse
import com.clinica.doors.outbound.database.dao.SpecialistDao
import com.clinic.model.SpecialistRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

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

    @Transactional
    fun create(request: SpecialistRequest): SpecialistResponse {
        check(!specialistDao.existsByEmail(request.email)) {
            "A specialist with email '${request.email}' already exists"
        }
        val specialist = Specialist(
            id = 0L,
            firstName = request.firstName,
            lastName = request.lastName,
            role = request.role,
            bio = request.bio,
            email = request.email,
            updatedAt = LocalDateTime.now()
        )
        return specialistDao.save(specialist).toResponse()
    }

    @Transactional
    fun update(id: Long, request: SpecialistRequest): SpecialistResponse {
        val existing = specialistDao.findById(id).orThrow("Specialist $id not found")
        require(!specialistDao.existsByEmailAndIdNot(request.email, id)) {
            "A specialist with email '${request.email}' already exists"
        }
        val updated = existing.copy(
            firstName = request.firstName,
            lastName = request.lastName,
            role = request.role,
            bio = request.bio,
            email = request.email,
            updatedAt = LocalDateTime.now()
        )
        return specialistDao.save(updated).toResponse()
    }

    @Transactional
    fun delete(id: Long) {
        specialistDao.findById(id).orThrow("Specialist $id not found")
        specialistDao.deleteById(id)
    }
}