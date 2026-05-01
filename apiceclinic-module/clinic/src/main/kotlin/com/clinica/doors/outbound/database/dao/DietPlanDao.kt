package com.clinica.doors.outbound.database.dao

import com.clinica.application.domain.DietPlan
import com.clinica.doors.outbound.database.entities.DietPlanEntity
import com.clinica.doors.outbound.database.entities.PatientEntity
import com.clinica.doors.outbound.database.entities.SpecialistEntity
import com.clinica.doors.outbound.database.mappers.toDomain
import com.clinica.doors.outbound.database.mappers.toEntity
import com.clinica.doors.outbound.database.repositories.DietPlanRepository
import com.clinica.doors.outbound.database.repositories.PatientRepository
import com.clinica.doors.outbound.database.repositories.SpecialistRepository
import org.springframework.stereotype.Component

@Component
class DietPlanDao(
    private val dietPlanRepository: DietPlanRepository,
    private val patientRepository: PatientRepository,
    private val specialistRepository: SpecialistRepository
) {

    /**
     * Se patientId è null, restituisce tutti i piani.
     * Se patientId è valorizzato, filtra per quel patient.
     */
    fun findAll(patientId: Long?): List<DietPlan> =
        if (patientId == null) {
            dietPlanRepository.findAll().map { it.toDomain() }
        } else {
            dietPlanRepository.findByPatientEntityId(patientId).map { it.toDomain() }
        }

    fun findById(id: Long): DietPlan? =
        dietPlanRepository.findById(id).orElse(null)?.toDomain()

    fun existsById(id: Long): Boolean =
        dietPlanRepository.existsById(id)

    fun save(dietPlan: DietPlan): DietPlan {
        val patientId = dietPlan.patient.id
        val specialistId = dietPlan.specialist.id

        val patientEntity: PatientEntity = patientRepository.findById(patientId)
            .orElseThrow { IllegalArgumentException("Patient not found with id: $patientId") }

        val specialistEntity: SpecialistEntity = specialistRepository.findById(specialistId)
            .orElseThrow { IllegalArgumentException("Specialist not found with id: $specialistId") }

        val existing: DietPlanEntity? =
            if (dietPlan.id != 0L) {
                dietPlanRepository.findById(dietPlan.id).orElse(null)
            } else {
                null
            }

        val entityToSave = dietPlan.toEntity(
            patientEntityProvider = { patientEntity },
            specialistEntityProvider = { specialistEntity },
            existingEntity = existing
        )

        val saved = dietPlanRepository.save(entityToSave)
        return saved.toDomain()
    }

    fun deleteById(id: Long) =
        dietPlanRepository.deleteById(id)
}