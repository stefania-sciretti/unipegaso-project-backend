package com.clinica.doors.outbound.database.repositories

import com.clinica.doors.outbound.database.entities.DoctorEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DoctorRepository : JpaRepository<DoctorEntity, Long>
