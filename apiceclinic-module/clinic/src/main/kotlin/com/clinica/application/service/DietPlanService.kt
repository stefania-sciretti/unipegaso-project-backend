package com.clinica.application.service

import com.clinica.application.domain.DietPlan
import com.clinica.application.service.DietPlanServicePort
import com.clinica.doors.outbound.database.dao.DietPlanDao
import com.clinica.doors.outbound.database.dao.PatientDao
import com.clinica.doors.outbound.database.dao.StaffDao
import com.clinica.dto.DietPlanRequest
import com.clinica.dto.DietPlanResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class DietPlanService(
    private val dietPlanDao: DietPlanDao,
    private val patientDao: PatientDao,
    private val staffDao: StaffDao
) : DietPlanServicePort {

    @Transactional(readOnly = true)
    override fun findAll(clientId: Long?): List<DietPlanResponse> =
        dietPlanDao.findAll(clientId)
            .map { it.toResponse() }

    @Transactional(readOnly = true)
    override fun findById(id: Long): DietPlanResponse {
        val dietPlan = dietPlanDao.findById(id)
            ?: throw NoSuchElementException("Diet plan not found with id: $id")
        return dietPlan.toResponse()
    }

    override fun create(request: DietPlanRequest): DietPlanResponse {
        val client = patientDao.findById(request.clientId)
            ?: throw NoSuchElementException("Client (patient) not found with id: ${request.clientId}")

        val trainer = staffDao.findById(request.trainerId)
            ?: throw NoSuchElementException("Trainer (staff) not found with id: ${request.trainerId}")

        val now = LocalDateTime.now()

        val dietPlan = DietPlan(
            id = 0L,
            client = client,
            trainer = trainer,
            title = request.title,
            description = request.description,
            calories = request.calories,
            durationWeeks = request.durationWeeks,
            active = request.active,
            createdAt = now,
            updatedAt = now
        )

        return dietPlanDao.save(dietPlan).toResponse()
    }

    override fun update(id: Long, request: DietPlanRequest): DietPlanResponse {
        val existing = dietPlanDao.findById(id)
            ?: throw NoSuchElementException("Diet plan not found with id: $id")

        val client = patientDao.findById(request.clientId)
            ?: throw NoSuchElementException("Client (patient) not found with id: ${request.clientId}")

        val trainer = staffDao.findById(request.trainerId)
            ?: throw NoSuchElementException("Trainer (staff) not found with id: ${request.trainerId}")

        val updated = existing.copy(
            client = client,
            trainer = trainer,
            title = request.title,
            description = request.description,
            calories = request.calories,
            durationWeeks = request.durationWeeks,
            active = request.active,
            updatedAt = LocalDateTime.now()
        )

        return dietPlanDao.save(updated).toResponse()
    }

    override fun delete(id: Long) {
        if (!dietPlanDao.existsById(id)) {
            throw NoSuchElementException("Diet plan not found with id: $id")
        }
        dietPlanDao.deleteById(id)
    }

    private fun DietPlan.toResponse(): DietPlanResponse =
        DietPlanResponse(
            id = this.id,
            clientId = this.client.id,
            trainerId = this.trainer.id,
            clientFirstName = this.client.firstName,
            clientLastName = this.client.lastName,
            trainerFirstName = this.trainer.firstName,
            trainerLastName = this.trainer.lastName,
            title = this.title,
            description = this.description,
            calories = this.calories,
            durationWeeks = this.durationWeeks,
            active = this.active,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
}