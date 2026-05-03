package com.clinica.application.service

import com.clinic.model.ServiceRequest
import com.clinica.application.domain.ClinicService
import com.clinica.doors.outbound.database.dao.ServiceDao
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class ClinicServiceService(
    private val serviceDao: ServiceDao
) {

    @Transactional(readOnly = true)
    fun findAll(): List<ClinicService> =
        serviceDao.findAll()

    @Transactional(readOnly = true)
    fun findBySpecialistId(specialistId: Long): List<ClinicService> =
        serviceDao.findBySpecialistId(specialistId)

    @Transactional(readOnly = true)
    fun findById(id: Long): ClinicService =
        serviceDao.findById(id).orThrow("Service $id not found")

    @Transactional
    fun create(request: ServiceRequest): ClinicService {
        val clinicService = ClinicService(
            id = 0L,
            service = request.service,
            price = BigDecimal.valueOf(request.price),
            specialistId = request.specialistId
        )
        return serviceDao.save(clinicService)
    }

    @Transactional
    fun update(id: Long, request: ServiceRequest): ClinicService {
        serviceDao.findById(id).orThrow("Service $id not found")
        val updated = ClinicService(
            id = id,
            service = request.service,
            price = BigDecimal.valueOf(request.price),
            specialistId = request.specialistId
        )
        return serviceDao.save(updated)
    }

    @Transactional
    fun delete(id: Long) {
        serviceDao.findById(id).orThrow("Service $id not found")
        serviceDao.deleteById(id)
    }
}
