package com.clinica.service

import com.clinica.domain.DietPlan
import com.clinica.dto.DietPlanRequest
import com.clinica.dto.DietPlanResponse
import com.clinica.repository.ClientRepository
import com.clinica.repository.DietPlanRepository
import com.clinica.repository.TrainerRepository
import com.clinica.service.api.DietPlanServicePort
import com.clinica.util.orEntityNotFound
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DietPlanService(
    private val dietPlanRepository: DietPlanRepository,
    private val clientRepository: ClientRepository,
    private val trainerRepository: TrainerRepository
) : DietPlanServicePort {

    @Transactional(readOnly = true)
    override fun findAll(clientId: Long?): List<DietPlanResponse> {
        val plans = if (clientId != null) dietPlanRepository.findByClientId(clientId)
                    else dietPlanRepository.findAll()
        return plans.map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): DietPlanResponse =
        dietPlanRepository.findById(id).orEntityNotFound("Diet plan", id).toResponse()

    override fun create(request: DietPlanRequest): DietPlanResponse {
        val client = clientRepository.findById(request.clientId)
            .orEntityNotFound("Client", request.clientId)
        val trainer = trainerRepository.findById(request.trainerId)
            .orEntityNotFound("Trainer", request.trainerId)

        val plan = DietPlan(
            client = client,
            trainer = trainer,
            title = request.title,
            description = request.description,
            calories = request.calories,
            durationWeeks = request.durationWeeks,
            active = request.active
        )
        return dietPlanRepository.save(plan).toResponse()
    }

    override fun update(id: Long, request: DietPlanRequest): DietPlanResponse {
        val plan = dietPlanRepository.findById(id).orEntityNotFound("Diet plan", id)

        plan.title = request.title
        plan.description = request.description
        plan.calories = request.calories
        plan.durationWeeks = request.durationWeeks
        plan.active = request.active

        return dietPlanRepository.save(plan).toResponse()
    }

    override fun delete(id: Long) {
        if (!dietPlanRepository.existsById(id)) {
            throw NoSuchElementException("Diet plan not found with id: $id")
        }
        dietPlanRepository.deleteById(id)
    }

    private fun DietPlan.toResponse() = DietPlanResponse(
        id = id,
        clientId = client.id,
        clientFullName = "${client.firstName} ${client.lastName}",
        trainerId = trainer.id,
        trainerFullName = "${trainer.firstName} ${trainer.lastName}",
        title = title,
        description = description,
        calories = calories,
        durationWeeks = durationWeeks,
        active = active,
        createdAt = createdAt
    )
}
