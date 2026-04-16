package com.clinica.doors.outbound.database.repositories

import com.clinica.doors.outbound.database.entities.StaffEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StaffRepository : JpaRepository<StaffEntity, Long> {

    fun findByRole(role: String): List<StaffEntity>
}