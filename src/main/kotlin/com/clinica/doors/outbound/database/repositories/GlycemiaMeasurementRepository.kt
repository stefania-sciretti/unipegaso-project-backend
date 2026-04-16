package com.clinica.repository
import com.clinica.domain.GlycemiaMeasurement
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
@Repository
interface GlycemiaMeasurementRepository : JpaRepository<GlycemiaMeasurement, Long> {
    fun findByClientIdOrderByMeasuredAtDesc(clientId: Long): List<GlycemiaMeasurement>
    fun findAllByOrderByMeasuredAtDesc(): List<GlycemiaMeasurement>
}
