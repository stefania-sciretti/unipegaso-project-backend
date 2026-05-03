package com.clinica.doors.outbound.database.repositories

import com.clinica.doors.outbound.database.entities.ServiceEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ServiceRepository : JpaRepository<ServiceEntity, Long> {

    fun findBySpecialistId(specialistId: Long): List<ServiceEntity>
}
