package com.clinica.doors.outbound.database.dao

import com.clinica.application.domain.ClinicService
import com.clinica.doors.outbound.database.mappers.toDomain
import com.clinica.doors.outbound.database.mappers.toEntity
import com.clinica.doors.outbound.database.repositories.ServiceRepository
import com.clinica.doors.outbound.database.repositories.SpecialistRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ServiceDao(
    private val serviceRepository: ServiceRepository,
    private val specialistRepository: SpecialistRepository
) {

    @Transactional(readOnly = true)
    fun findAll(): List<ClinicService> =
        serviceRepository.findAll().map { it.toDomain() }

    @Transactional(readOnly = true)
    fun findBySpecialistId(specialistId: Long): List<ClinicService> =
        serviceRepository.findBySpecialistId(specialistId).map { it.toDomain() }

    @Transactional(readOnly = true)
    fun findById(id: Long): ClinicService? =
        serviceRepository.findById(id).orElse(null)?.toDomain()

    @Transactional
    fun save(clinicService: ClinicService): ClinicService {
        val specialistEntity = specialistRepository.findById(clinicService.specialistId).orElse(null)
            ?: throw NoSuchElementException("Specialist ${clinicService.specialistId} not found")
        val entity = clinicService.toEntity(specialistEntity, specialistEntity.area)
        return serviceRepository.save(entity).toDomain()
    }

    fun deleteById(id: Long) =
        serviceRepository.deleteById(id)
}
