package com.clinica.application.service

import com.clinica.doors.outbound.database.entities.StaffEntity
import com.clinica.doors.outbound.database.repositories.StaffRepository
import com.clinica.dto.StaffResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StaffService(
    private val staffRepository: StaffRepository
) {

    @Transactional(readOnly = true)
    fun findAll(): List<StaffResponse> =
        staffRepository.findAll().map { it.toResponse() }

    @Transactional(readOnly = true)
    fun findByRole(role: String): List<StaffResponse> =
        staffRepository.findByRole(role).map { it.toResponse() }

    @Transactional(readOnly = true)
    fun findById(id: Long): StaffResponse {
        val staff = staffRepository.findById(id)
            .orElseThrow { NoSuchElementException("Staff member $id not found") }
        return staff.toResponse()
    }

    private fun StaffEntity.toResponse(): StaffResponse =
        StaffResponse(
            id = id,
            firstName = firstName,
            lastName = lastName,
            role = role,
            bio = bio,
            email = email,
            createdAt = createdAt
        )
}