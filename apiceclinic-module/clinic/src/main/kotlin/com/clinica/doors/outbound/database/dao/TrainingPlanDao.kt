package com.clinica.doors.outbound.database.dao

import com.clinica.application.domain.TrainingPlan
import com.clinica.doors.outbound.database.entities.PatientEntity
import com.clinica.doors.outbound.database.entities.SpecialistEntity
import com.clinica.doors.outbound.database.mappers.toDomain
import com.clinica.doors.outbound.database.mappers.toEntity
import com.clinica.doors.outbound.database.repositories.PatientRepository
import com.clinica.doors.outbound.database.repositories.SpecialistRepository
import com.clinica.doors.outbound.database.repositories.TrainingPlanRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class TrainingPlanDao(
    private val trainingPlanRepository: TrainingPlanRepository,
    private val patientRepository: PatientRepository,
    private val specialistRepository: SpecialistRepository
) {

    @Transactional(readOnly = true)
    fun findAll(patientId: Long?): List<TrainingPlan> =
        if (patientId == null) trainingPlanRepository.findAll().map { it.toDomain() }
        else trainingPlanRepository.findByPatientEntityId(patientId).map { it.toDomain() }

    @Transactional(readOnly = true)
    fun findById(id: Long): TrainingPlan? =
        trainingPlanRepository.findById(id).orElse(null)?.toDomain()

    @Transactional(readOnly = true)
    fun existsById(id: Long): Boolean =
        trainingPlanRepository.existsById(id)

    @Transactional
    fun save(plan: TrainingPlan): TrainingPlan {
        val patientEntity: PatientEntity = patientRepository.findById(plan.patient.id)
            .orElseThrow { IllegalArgumentException("Patient not found with id: ${plan.patient.id}") }
        val specialistEntity: SpecialistEntity = specialistRepository.findById(plan.specialist.id)
            .orElseThrow { IllegalArgumentException("Specialist not found with id: ${plan.specialist.id}") }

        val existing = if (plan.id != 0L)
            trainingPlanRepository.findById(plan.id).orElse(null) else null

        val entityToSave = plan.toEntity(
            patientEntityProvider = { patientEntity },
            specialistEntityProvider = { specialistEntity },
            existingEntity = existing
        )
        return trainingPlanRepository.save(entityToSave).toDomain()
    }

    @Transactional
    fun deleteById(id: Long) =
        trainingPlanRepository.deleteById(id)
}
