package com.clinica.service

import com.clinica.domain.Staff
import com.clinica.dto.StaffResponse
import com.clinica.repository.StaffRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class StaffService(private val staffRepository: StaffRepository) {

    fun findAll(): List<StaffResponse> =
        staffRepository.findAll().map { it.toResponse() }

    fun findById(id: Long): StaffResponse =
        staffRepository.findById(id)
            .orElseThrow { NoSuchElementException("Staff not found with id: $id") }
            .toResponse()

    fun findByRole(role: String): List<StaffResponse> =
        staffRepository.findByRole(role.uppercase()).map { it.toResponse() }

    private fun Staff.toResponse() = StaffResponse(
        id = id,
        firstName = firstName,
        lastName = lastName,
        role = role,
        bio = bio,
        email = email,
        createdAt = createdAt
    )
}
