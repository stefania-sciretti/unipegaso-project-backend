package com.clinica.doors.outbound.database.repositories

import com.clinica.doors.outbound.database.entities.PatientEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientRepository : JpaRepository<PatientEntity, Long>
