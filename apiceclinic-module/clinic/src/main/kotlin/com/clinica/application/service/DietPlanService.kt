package com.clinica.application.service

import com.clinica.application.domain.DietPlan
import com.clinica.doors.outbound.database.dao.DietPlanDao
import com.clinica.doors.outbound.database.dao.PatientDao
import com.clinica.doors.outbound.database.dao.SpecialistDao
import com.clinic.model.DietPlanRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class DietPlanService(
    private val dietPlanDao: DietPlanDao,
    private val patientDao: PatientDao,
    private val specialistDao: SpecialistDao
)  {

    @Transactional(readOnly = true)
    fun findAll(patientId: Long?): List<DietPlan> =
        dietPlanDao.findAll(patientId)

    @Transactional(readOnly = true)
    fun findById(id: Long): DietPlan =
        dietPlanDao.findById(id).orThrow("Diet plan not found with id: $id")

    @Transactional
    fun create(request: DietPlanRequest): DietPlan {
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
            active = request.active ?: true,
            createdAt = now,
            updatedAt = now
        )

        return dietPlanDao.save(dietPlan)
    }

    @Transactional
    fun update(id: Long, request: DietPlanRequest): DietPlan {
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
            active = request.active ?: true,
            updatedAt = LocalDateTime.now()
        )

        return dietPlanDao.save(updated)
    }

    @Transactional
    fun delete(id: Long) {
        dietPlanDao.findById(id).orThrow("Diet plan not found with id: $id")
        dietPlanDao.deleteById(id)
    }
}