package com.clinica.doors.inbound.routes.controller

import com.clinic.model.ServiceRequest
import com.clinic.model.ServiceResponse
import com.clinica.application.mappers.toResponse
import com.clinica.application.service.ClinicServiceService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/services")
@Tag(name = "Services", description = "Service catalog management")
class ServiceController(private val clinicServiceService: ClinicServiceService) {

    @GetMapping
    @Operation(summary = "List all services", description = "Optionally filter by specialist ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List returned"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun findAll(
        @Parameter(description = "Filter by specialist ID")
        @RequestParam(required = false) specialistId: Long?
    ): List<ServiceResponse> =
        if (specialistId != null) clinicServiceService.findBySpecialistId(specialistId).map { it.toResponse() }
        else clinicServiceService.findAll().map { it.toResponse() }

    @GetMapping("/{id}")
    @Operation(summary = "Get service by ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Service found"),
        ApiResponse(responseCode = "404", description = "Service not found"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun findById(
        @Parameter(description = "Service ID") @PathVariable id: Long
    ): ServiceResponse =
        clinicServiceService.findById(id).toResponse()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new service")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Service created"),
        ApiResponse(responseCode = "400", description = "Validation error"),
        ApiResponse(responseCode = "404", description = "Specialist not found"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun create(@Valid @RequestBody request: ServiceRequest): ServiceResponse =
        clinicServiceService.create(request).toResponse()

    @PutMapping("/{id}")
    @Operation(summary = "Update a service")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Service updated"),
        ApiResponse(responseCode = "400", description = "Validation error"),
        ApiResponse(responseCode = "404", description = "Service or Specialist not found"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun update(
        @Parameter(description = "Service ID") @PathVariable id: Long,
        @Valid @RequestBody request: ServiceRequest
    ): ServiceResponse =
        clinicServiceService.update(id, request).toResponse()

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a service")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Service deleted"),
        ApiResponse(responseCode = "404", description = "Service not found"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun delete(
        @Parameter(description = "Service ID") @PathVariable id: Long
    ) = clinicServiceService.delete(id)
}
