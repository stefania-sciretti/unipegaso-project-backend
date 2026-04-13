package com.clinica.controller

import com.clinica.dto.FitnessAppointmentRequest
import com.clinica.dto.FitnessAppointmentResponse
import com.clinica.dto.FitnessAppointmentStatusRequest
import com.clinica.service.api.FitnessAppointmentServicePort
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Appointments", description = "Fitness appointment booking and management")
class FitnessAppointmentController(private val appointmentService: FitnessAppointmentServicePort) {

    @GetMapping
    @Operation(summary = "List appointments", description = "Filter by clientId, trainerId or status")
    fun findAll(
        @RequestParam(required = false) clientId: Long?,
        @RequestParam(required = false) trainerId: Long?,
        @RequestParam(required = false) status: String?
    ): List<FitnessAppointmentResponse> = appointmentService.findAll(clientId, trainerId, status)

    @GetMapping("/{id}")
    @Operation(summary = "Get appointment by ID")
    fun findById(@PathVariable id: Long): FitnessAppointmentResponse =
        appointmentService.findById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Book a new appointment")
    fun create(@Valid @RequestBody request: FitnessAppointmentRequest): FitnessAppointmentResponse =
        appointmentService.create(request)

    @PutMapping("/{id}/status")
    @Operation(summary = "Update appointment status")
    fun updateStatus(
        @PathVariable id: Long,
        @Valid @RequestBody request: FitnessAppointmentStatusRequest
    ): FitnessAppointmentResponse = appointmentService.updateStatus(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cancel an appointment")
    fun delete(@PathVariable id: Long) = appointmentService.delete(id)
}
