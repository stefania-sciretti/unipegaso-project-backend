package com.clinica.doors.outbound.database.repositories

import com.clinica.doors.outbound.database.entities.GlycemiaMeasurementEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GlycemiaMeasurementRepository : JpaRepository<GlycemiaMeasurementEntity, Long> {

    fun findByPatientEntityIdOrderByMeasuredAtDesc(patientId: Long): List<GlycemiaMeasurementEntity>

    fun findAllByOrderByMeasuredAtDesc(): List<GlycemiaMeasurementEntity>
}
