package com.clinica.controller

import com.clinica.application.service.FitnessAppointmentService
import com.clinica.dto.FitnessAppointmentRequest
import com.clinica.dto.FitnessAppointmentResponse
import com.clinica.dto.FitnessAppointmentStatusRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/fitness-appointments")
@Tag(name = "Fitness Appointments", description = "Fitness appointment booking and management")
class FitnessAppointmentController(private val fitnessAppointmentService: FitnessAppointmentService) {

    @GetMapping
    @Operation(summary = "List appointments")
    fun findAll(
        @RequestParam(required = false) clientId: Long?,
        @RequestParam(required = false) trainerId: Long?,
        @RequestParam(required = false) status: String?
    ): List<FitnessAppointmentResponse> = fitnessAppointmentService.findAll(clientId, trainerId, status)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Book a new appointment")
    fun create(@Valid @RequestBody request: FitnessAppointmentRequest): FitnessAppointmentResponse =
        fitnessAppointmentService.create(request)

    @GetMapping("/{id}")
    @Operation(summary = "Get appointment by ID")
    fun findById(@PathVariable id: Long): FitnessAppointmentResponse =
        fitnessAppointmentService.findById(id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cancel an appointment")
    fun delete(@PathVariable id: Long) = fitnessAppointmentService.delete(id)

    @PutMapping("/{id}/status")
    @Operation(summary = "Update appointment status")
    fun updateStatus(
        @PathVariable id: Long,
        @Valid @RequestBody request: FitnessAppointmentStatusRequest
    ): FitnessAppointmentResponse = fitnessAppointmentService.updateStatus(id, request)
}
