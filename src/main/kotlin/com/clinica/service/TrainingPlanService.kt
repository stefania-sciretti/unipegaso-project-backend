package com.clinica.service

import com.clinica.domain.TrainingPlan
import com.clinica.dto.TrainingPlanRequest
import com.clinica.dto.TrainingPlanResponse
import com.clinica.repository.ClientRepository
import com.clinica.repository.TrainerRepository
import com.clinica.repository.TrainingPlanRepository
import com.clinica.service.api.TrainingPlanServicePort
import com.clinica.util.orEntityNotFound
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TrainingPlanService(
    private val trainingPlanRepository: TrainingPlanRepository,
    private val clientRepository: ClientRepository,
    private val trainerRepository: TrainerRepository
) : TrainingPlanServicePort {

    @Transactional(readOnly = true)
    override fun findAll(clientId: Long?): List<TrainingPlanResponse> {
        val plans = if (clientId != null) trainingPlanRepository.findByClientId(clientId)
                    else trainingPlanRepository.findAll()
        return plans.map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): TrainingPlanResponse =
        trainingPlanRepository.findById(id).orEntityNotFound("Training plan", id).toResponse()

    override fun create(request: TrainingPlanRequest): TrainingPlanResponse {
        val client = clientRepository.findById(request.clientId)
            .orEntityNotFound("Client", request.clientId)
        val trainer = trainerRepository.findById(request.trainerId)
            .orEntityNotFound("Trainer", request.trainerId)

        val plan = TrainingPlan(
            client = client,
            trainer = trainer,
            title = request.title,
            description = request.description,
            weeks = request.weeks,
            sessionsPerWeek = request.sessionsPerWeek,
            active = request.active
        )
        return trainingPlanRepository.save(plan).toResponse()
    }

    override fun update(id: Long, request: TrainingPlanRequest): TrainingPlanResponse {
        val plan = trainingPlanRepository.findById(id).orEntityNotFound("Training plan", id)

        plan.title = request.title
        plan.description = request.description
        plan.weeks = request.weeks
        plan.sessionsPerWeek = request.sessionsPerWeek
        plan.active = request.active

        return trainingPlanRepository.save(plan).toResponse()
    }

    override fun delete(id: Long) {
        if (!trainingPlanRepository.existsById(id)) {
            throw NoSuchElementException("Training plan not found with id: $id")
        }
        trainingPlanRepository.deleteById(id)
    }

    private fun TrainingPlan.toResponse() = TrainingPlanResponse(
        id = id,
        clientId = client.id,
        clientFullName = "${client.firstName} ${client.lastName}",
        trainerId = trainer.id,
        trainerFullName = "${trainer.firstName} ${trainer.lastName}",
        title = title,
        description = description,
        weeks = weeks,
        sessionsPerWeek = sessionsPerWeek,
        active = active,
        createdAt = createdAt
    )
}
