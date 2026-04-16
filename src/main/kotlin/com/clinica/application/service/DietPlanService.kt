package com.clinica.service

import com.clinica.domain.DietPlanEntity
import com.clinica.dto.DietPlanRequest
import com.clinica.dto.DietPlanResponse
import com.clinica.repository.ClientRepository
import com.clinica.repository.DietPlanRepository
import com.clinica.repository.StaffRepository
import com.clinica.service.api.DietPlanServicePort
import com.clinica.util.orEntityNotFound
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DietPlanService(
    private val dietPlanRepository: DietPlanRepository,
    private val clientRepository: ClientRepository,
    private val staffRepository: StaffRepository
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
        val staff = staffRepository.findById(request.trainerId)
            .orEntityNotFound("Staff", request.trainerId)

        val plan = DietPlanEntity(
            clientEntity = client,
            staff = staff,
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

    private fun DietPlanEntity.toResponse() = DietPlanResponse(
        id = id,
        clientId = clientEntity.id,
        clientFullName = "${clientEntity.firstName} ${clientEntity.lastName}",
        trainerId = staff.id,
        trainerFullName = "${staff.firstName} ${staff.lastName}",
        title = title,
        description = description,
        calories = calories,
        durationWeeks = durationWeeks,
        active = active,
        createdAt = createdAt
    )
}
