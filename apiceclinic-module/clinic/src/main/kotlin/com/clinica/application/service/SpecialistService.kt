package com.clinica.application.service

import com.clinic.model.SpecialistRequest
import com.clinica.application.domain.Specialist
import com.clinica.doors.outbound.database.dao.SpecialistDao
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class SpecialistService(
    private val specialistDao: SpecialistDao
) {

    @Transactional(readOnly = true)
    fun findAll(): List<Specialist> =
        specialistDao.findAll()

    @Transactional(readOnly = true)
    fun findByRole(role: String): List<Specialist> =
        specialistDao.findByRole(role)

    @Transactional(readOnly = true)
    fun findById(id: Long): Specialist =
        specialistDao.findById(id).orThrow("Specialist member $id not found")

    @Transactional
    fun create(request: SpecialistRequest): Specialist {
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
            areaId = areaIdForRole(request.role),
            updatedAt = LocalDateTime.now()
        )
        return specialistDao.save(specialist)
    }

    @Transactional
    fun update(id: Long, request: SpecialistRequest): Specialist {
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
            areaId = areaIdForRole(request.role),
            updatedAt = LocalDateTime.now()
        )
        return specialistDao.save(updated)
    }

    @Transactional
    fun delete(id: Long) {
        specialistDao.findById(id).orThrow("Specialist $id not found")
        specialistDao.deleteById(id)
    }

    companion object {
        /**
         * Derives the area ID from the specialist's role.
         * Returns null for unknown roles so that unmapped roles are not silently assigned.
         */
        fun areaIdForRole(role: String): Long? = when (role.uppercase()) {
            "NUTRITIONIST", "DIETOLOGIST" -> 1L
            "PERSONAL_TRAINER"            -> 2L
            "SPORT_DOCTOR", "PHYSIOTHERAPIST" -> 3L
            else -> null
        }
    }
}