package com.clinica.doors.outbound.database.dao

import com.clinica.application.domain.DietPlan
import com.clinica.doors.outbound.database.entities.DietPlanEntity
import com.clinica.doors.outbound.database.entities.PatientEntity
import com.clinica.doors.outbound.database.entities.StaffEntity
import com.clinica.doors.outbound.database.mappers.toDomain
import com.clinica.doors.outbound.database.mappers.toEntity
import com.clinica.doors.outbound.database.repositories.DietPlanRepository
import com.clinica.doors.outbound.database.repositories.PatientRepository
import com.clinica.doors.outbound.database.repositories.StaffRepository
import org.springframework.stereotype.Component

@Component
class DietPlanDao(
    private val dietPlanRepository: DietPlanRepository,
    private val patientRepository: PatientRepository,
    private val staffRepository: StaffRepository
) {

    /**
     * Se clientId è null, restituisce tutti i piani.
     * Se clientId è valorizzato, filtra per quel client.
     */
    fun findAll(clientId: Long?): List<DietPlan> =
        if (clientId == null) {
            dietPlanRepository.findAll().map { it.toDomain() }
        } else {
            dietPlanRepository.findByClientEntityId(clientId).map { it.toDomain() }
        }

    fun findById(id: Long): DietPlan? =
        dietPlanRepository.findById(id).orElse(null)?.toDomain()

    fun existsById(id: Long): Boolean =
        dietPlanRepository.existsById(id)

    fun save(dietPlan: DietPlan): DietPlan {
        // recupero le entity associate da DB
        val clientId = dietPlan.client.id
        val trainerId = dietPlan.trainer.id

        val clientEntity: PatientEntity = patientRepository.findById(clientId)
            .orElseThrow { IllegalArgumentException("Client (patient) not found with id: $clientId") }

        val staffEntity: StaffEntity = staffRepository.findById(trainerId)
            .orElseThrow { IllegalArgumentException("Trainer (staff) not found with id: $trainerId") }

        // se è update, prendo l'entity esistente
        val existing: DietPlanEntity? =
            if (dietPlan.id != 0L) {
                dietPlanRepository.findById(dietPlan.id).orElse(null)
            } else {
                null
            }

        val entityToSave = dietPlan.toEntity(
            clientEntityProvider = { clientEntity },
            staffEntityProvider = { staffEntity },
            existingEntity = existing
        )

        val saved = dietPlanRepository.save(entityToSave)
        return saved.toDomain()
    }

    fun deleteById(id: Long) =
        dietPlanRepository.deleteById(id)
}