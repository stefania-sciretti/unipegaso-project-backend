package com.clinica.repository

import com.clinica.domain.Staff
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StaffRepository : JpaRepository<Staff, Long> {
    fun findByRole(role: String): List<Staff>
}
