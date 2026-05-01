package com.clinica.application.service

import com.clinica.application.domain.DietPlan
import com.clinica.doors.outbound.database.dao.DietPlanDao
import com.clinica.doors.outbound.database.dao.PatientDao
import com.clinica.doors.outbound.database.dao.SpecialistDao
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
    private val specialistDao: SpecialistDao
)  {

    @Transactional(readOnly = true)
    fun findAll(patientId: Long?): List<DietPlanResponse> =
        dietPlanDao.findAll(patientId)
            .map { it.toResponse() }

    @Transactional(readOnly = true)
    fun findById(id: Long): DietPlanResponse =
        dietPlanDao.findById(id).orThrow("Diet plan not found with id: $id").toResponse()

    fun create(request: DietPlanRequest): DietPlanResponse {
        val patient = patientDao.findById(request.patientId)
            .orThrow("Patient not found with id: ${request.patientId}")

        val specialist = specialistDao.findById(request.specialistId)
            .orThrow("Specialist not found with id: ${request.specialistId}")

        val now = LocalDateTime.now()

        val dietPlan = DietPlan(
            id = 0L,
            patient = patient,
            specialist = specialist,
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

    fun update(id: Long, request: DietPlanRequest): DietPlanResponse {
        val existing = dietPlanDao.findById(id)
            .orThrow("Diet plan not found with id: $id")

        val patient = patientDao.findById(request.patientId)
            .orThrow("Patient not found with id: ${request.patientId}")

        val specialist = specialistDao.findById(request.specialistId)
            .orThrow("Specialist not found with id: ${request.specialistId}")

        val updated = existing.copy(
            patient = patient,
            specialist = specialist,
            title = request.title,
            description = request.description,
            calories = request.calories,
            durationWeeks = request.durationWeeks,
            active = request.active,
            updatedAt = LocalDateTime.now()
        )

        return dietPlanDao.save(updated).toResponse()
    }

    fun delete(id: Long) {
        dietPlanDao.findById(id).orThrow("Diet plan not found with id: $id")
        dietPlanDao.deleteById(id)
    }

    private fun DietPlan.toResponse(): DietPlanResponse =
        DietPlanResponse(
            id = this.id,
            patientId = this.patient.id,
            specialistId = this.specialist.id,
            patientFirstName = this.patient.firstName,
            patientLastName = this.patient.lastName,
            specialistFirstName = this.specialist.firstName,
            specialistLastName = this.specialist.lastName,
            title = this.title,
            description = this.description,
            calories = this.calories,
            durationWeeks = this.durationWeeks,
            active = this.active,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
}