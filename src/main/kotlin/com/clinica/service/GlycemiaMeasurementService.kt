package com.clinica.service

import com.clinica.domain.GlycemiaClassifier
import com.clinica.domain.GlycemiaMeasurement
import com.clinica.dto.GlycemiaMeasurementRequest
import com.clinica.dto.GlycemiaMeasurementResponse
import com.clinica.repository.ClientRepository
import com.clinica.repository.GlycemiaMeasurementRepository
import com.clinica.repository.TrainerRepository
import com.clinica.service.api.GlycemiaMeasurementServicePort
import com.clinica.util.orEntityNotFound
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class GlycemiaMeasurementService(
    private val glycemiaRepository: GlycemiaMeasurementRepository,
    private val clientRepository: ClientRepository,
    private val trainerRepository: TrainerRepository
) : GlycemiaMeasurementServicePort {

    @Transactional(readOnly = true)
    override fun findAll(clientId: Long?): List<GlycemiaMeasurementResponse> {
        val measurements = if (clientId != null) {
            glycemiaRepository.findByClientIdOrderByMeasuredAtDesc(clientId)
        } else {
            glycemiaRepository.findAllByOrderByMeasuredAtDesc()
        }
        return measurements.map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): GlycemiaMeasurementResponse =
        glycemiaRepository.findById(id).orEntityNotFound("Glycemia measurement", id).toResponse()

    override fun create(request: GlycemiaMeasurementRequest): GlycemiaMeasurementResponse {
        val client = clientRepository.findById(request.clientId)
            .orEntityNotFound("Client", request.clientId)
        val trainer = trainerRepository.findById(request.trainerId)
            .orEntityNotFound("Trainer", request.trainerId)

        val measurement = GlycemiaMeasurement(
            client = client,
            trainer = trainer,
            measuredAt = request.measuredAt,
            valueMgDl = request.valueMgDl,
            context = request.context,
            notes = request.notes
        )
        return glycemiaRepository.save(measurement).toResponse()
    }

    override fun update(id: Long, request: GlycemiaMeasurementRequest): GlycemiaMeasurementResponse {
        val measurement = glycemiaRepository.findById(id).orEntityNotFound("Glycemia measurement", id)

        measurement.measuredAt = request.measuredAt
        measurement.valueMgDl = request.valueMgDl
        measurement.context = request.context
        measurement.notes = request.notes

        return glycemiaRepository.save(measurement).toResponse()
    }

    override fun delete(id: Long) {
        if (!glycemiaRepository.existsById(id)) {
            throw NoSuchElementException("Glycemia measurement not found with id: $id")
        }
        glycemiaRepository.deleteById(id)
    }

    private fun GlycemiaMeasurement.toResponse() = GlycemiaMeasurementResponse(
        id = id,
        clientId = client.id,
        clientFullName = "${client.firstName} ${client.lastName}",
        trainerId = trainer.id,
        trainerFullName = "${trainer.firstName} ${trainer.lastName}",
        measuredAt = measuredAt,
        valueMgDl = valueMgDl,
        context = context,
        classification = GlycemiaClassifier.classify(valueMgDl, context),
        notes = notes,
        createdAt = createdAt
    )
}
