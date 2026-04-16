package com.clinica.application.service

import com.clinica.doors.outbound.database.entities.TrainingPlanEntity
import com.clinica.doors.outbound.database.repositories.ClientRepository
import com.clinica.doors.outbound.database.repositories.StaffRepository
import com.clinica.doors.outbound.database.repositories.TrainingPlanRepository
import com.clinica.dto.TrainingPlanRequest
import com.clinica.dto.TrainingPlanResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TrainingPlanService(
    private val trainingPlanRepository: TrainingPlanRepository,
    private val clientRepository: ClientRepository,
    private val staffRepository: StaffRepository
) : TrainingPlanServicePort {

    @Transactional(readOnly = true)
    override fun findAll(clientId: Long?): List<TrainingPlanResponse> {
        val plans = if (clientId != null) trainingPlanRepository.findByClientEntityId(clientId)
                    else trainingPlanRepository.findAll()
        return plans.map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): TrainingPlanResponse =
        trainingPlanRepository.findById(id).orElse(null)?.toResponse()
            ?: throw NoSuchElementException("Training plan not found with id: $id")

    override fun create(request: TrainingPlanRequest): TrainingPlanResponse {
        val client = clientRepository.findById(request.clientId)
            .orElseThrow { NoSuchElementException("Client not found with id: ${request.clientId}") }
        val staff = staffRepository.findById(request.trainerId)
            .orElseThrow { NoSuchElementException("Staff not found with id: ${request.trainerId}") }

        val plan = TrainingPlanEntity(
            clientEntity = client,
            staff = staff,
            title = request.title,
            description = request.description,
            weeks = request.weeks,
            sessionsPerWeek = request.sessionsPerWeek,
            active = request.active
        )
        return trainingPlanRepository.save(plan).toResponse()
    }

    override fun update(id: Long, request: TrainingPlanRequest): TrainingPlanResponse {
        val plan = trainingPlanRepository.findById(id).orElse(null)
            ?: throw NoSuchElementException("Training plan not found with id: $id")
        plan.title = request.title
        plan.description = request.description
        plan.weeks = request.weeks
        plan.sessionsPerWeek = request.sessionsPerWeek
        plan.active = request.active
        return trainingPlanRepository.save(plan).toResponse()
    }

    override fun delete(id: Long) {
        if (!trainingPlanRepository.existsById(id))
            throw NoSuchElementException("Training plan not found with id: $id")
        trainingPlanRepository.deleteById(id)
    }

    private fun TrainingPlanEntity.toResponse() = TrainingPlanResponse(
        id = id,
        clientId = clientEntity.id,
        clientFullName = "${clientEntity.firstName} ${clientEntity.lastName}",
        trainerId = staff.id,
        trainerFullName = "${staff.firstName} ${staff.lastName}",
        title = title,
        description = description,
        weeks = weeks,
        sessionsPerWeek = sessionsPerWeek,
        active = active,
        createdAt = createdAt
    )
}
