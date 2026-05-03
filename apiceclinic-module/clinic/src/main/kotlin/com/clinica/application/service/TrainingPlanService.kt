package com.clinica.application.service

import com.clinica.application.domain.TrainingPlan
import com.clinica.doors.outbound.database.dao.PatientDao
import com.clinica.doors.outbound.database.dao.SpecialistDao
import com.clinica.doors.outbound.database.dao.TrainingPlanDao
import com.clinic.model.TrainingPlanRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TrainingPlanService(
    private val trainingPlanDao: TrainingPlanDao,
    private val patientDao: PatientDao,
    private val specialistDao: SpecialistDao
) : TrainingPlanServicePort {

    @Transactional(readOnly = true)
    override fun findAll(patientId: Long?): List<TrainingPlan> =
        trainingPlanDao.findAll(patientId)

    @Transactional(readOnly = true)
    override fun findById(id: Long): TrainingPlan =
        trainingPlanDao.findById(id).orThrow("Training plan not found with id: $id")

    override fun create(request: TrainingPlanRequest): TrainingPlan {
        val patient = patientDao.findById(request.patientId)
            .orThrow("Patient not found with id: ${request.patientId}")
        val specialist = specialistDao.findById(request.specialistId)
            .orThrow("Specialist not found with id: ${request.specialistId}")

        val plan = TrainingPlan(
            patient = patient,
            specialist = specialist,
            title = request.title,
            description = request.description,
            weeks = request.weeks,
            sessionsPerWeek = request.sessionsPerWeek,
            active = request.active ?: true
        )
        return trainingPlanDao.save(plan)
    }

    override fun update(id: Long, request: TrainingPlanRequest): TrainingPlan {
        trainingPlanDao.findById(id).orThrow("Training plan not found with id: $id")

        val patient = patientDao.findById(request.patientId)
            .orThrow("Patient not found with id: ${request.patientId}")
        val specialist = specialistDao.findById(request.specialistId)
            .orThrow("Specialist not found with id: ${request.specialistId}")

        val updated = TrainingPlan(
            id = id,
            patient = patient,
            specialist = specialist,
            title = request.title,
            description = request.description,
            weeks = request.weeks,
            sessionsPerWeek = request.sessionsPerWeek,
            active = request.active ?: true
        )
        return trainingPlanDao.save(updated)
    }

    override fun delete(id: Long) {
        trainingPlanDao.findById(id).orThrow("Training plan not found with id: $id")
        trainingPlanDao.deleteById(id)
    }
}
