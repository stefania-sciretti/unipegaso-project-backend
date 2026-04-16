package com.clinica.doors.outbound.database.dao

import com.clinica.application.domain.Staff
import com.clinica.doors.outbound.database.mappers.toDomain
import com.clinica.doors.outbound.database.repositories.StaffRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class StaffDao(
    private val staffRepository: StaffRepository
) {

    @Transactional(readOnly = true)
    fun findAll(): List<Staff> =
        staffRepository.findAll().map { it.toDomain() }

    @Transactional(readOnly = true)
    fun findByRole(role: String): List<Staff> =
        staffRepository.findByRole(role).map { it.toDomain() }

    @Transactional(readOnly = true)
    fun findById(id: Long): Staff? =
        staffRepository.findById(id).orElse(null)?.toDomain()
}