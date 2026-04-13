package com.clinica.service.api

import com.clinica.dto.GlycemiaMeasurementRequest
import com.clinica.dto.GlycemiaMeasurementResponse

/**
 * Contract for glycemia measurement operations.
 */
interface GlycemiaMeasurementServicePort {
    fun findAll(clientId: Long?): List<GlycemiaMeasurementResponse>
    fun findById(id: Long): GlycemiaMeasurementResponse
    fun create(request: GlycemiaMeasurementRequest): GlycemiaMeasurementResponse
    fun update(id: Long, request: GlycemiaMeasurementRequest): GlycemiaMeasurementResponse
    fun delete(id: Long)
}
