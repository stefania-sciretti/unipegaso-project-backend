package com.clinica.doors.outbound.database.dao

import com.clinica.application.domain.Doctor
import com.clinica.doors.outbound.database.mappers.toDomain
import com.clinica.doors.outbound.database.mappers.toEntity
import com.clinica.doors.outbound.database.repositories.DoctorRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DoctorDao(
    private val doctorRepository: DoctorRepository
) {

    @Transactional(readOnly = true)
    fun findById(id: Long): Doctor? =
        doctorRepository.findById(id).orElse(null)?.toDomain()

    @Transactional(readOnly = true)
    fun existsById(id: Long): Boolean =
        doctorRepository.existsById(id)

    @Transactional
    fun save(doctor: Doctor): Doctor =
        doctorRepository.save(doctor.toEntity()).toDomain()

    @Transactional
    fun deleteById(id: Long) =
        doctorRepository.deleteById(id)
}
