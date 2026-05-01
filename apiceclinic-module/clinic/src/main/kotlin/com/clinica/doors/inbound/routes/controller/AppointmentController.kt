package com.clinica.controller

import com.clinica.application.service.AppointmentService
import com.clinica.dto.AppointmentRequest
import com.clinica.dto.AppointmentResponse
import com.clinica.dto.AppointmentStatusRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Appointments", description = "Clinical appointment management")
class AppointmentController(
    private val appointmentService: AppointmentService
) {

    @GetMapping
    @Operation(
        summary = "List all appointments",
        description = "List all appointments, optionally filtered by patientId, doctorId or status"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List returned"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun findAll(
        @RequestParam(required = false) patientId: Long?,
        @RequestParam(required = false) doctorId: Long?,
        @RequestParam(required = false) status: String?
    ): List<AppointmentResponse> =
        appointmentService.findAll(patientId, doctorId, status)

    @GetMapping("/{id}")
    @Operation(summary = "Get appointment by ID", description = "Returns an appointment by its ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Appointment found"),
        ApiResponse(responseCode = "404", description = "Appointment not found"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun findById(@PathVariable id: Long): AppointmentResponse =
        appointmentService.findById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new appointment", description = "Creates a new clinical appointment")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Appointment created"),
        ApiResponse(responseCode = "400", description = "Validation error"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun create(@Valid @RequestBody request: AppointmentRequest): AppointmentResponse =
        appointmentService.create(request)

    @PatchMapping("/{id}/status")
    @Operation(
        summary = "Update appointment status",
        description = "Updates the status of an existing appointment"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Status updated"),
        ApiResponse(responseCode = "400", description = "Validation error"),
        ApiResponse(responseCode = "404", description = "Appointment not found"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun updateStatus(
        @PathVariable id: Long,
        @Valid @RequestBody request: AppointmentStatusRequest
    ): AppointmentResponse =
        appointmentService.updateStatus(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cancel an appointment", description = "Cancels an appointment by its ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Appointment cancelled"),
        ApiResponse(responseCode = "404", description = "Appointment not found"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun delete(@PathVariable id: Long) =
        appointmentService.delete(id)
}