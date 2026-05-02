package com.clinica.doors.inbound.routes.controller

import com.clinica.application.service.SpecialistService
import com.clinica.dto.SpecialistRequest
import com.clinic.model.SpecialistResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/specialists")
@Tag(name = "Specialists", description = "Specialist management")
class SpecialistController(private val specialistService: SpecialistService) {

    @GetMapping
    @Operation(summary = "List all specialists", description = "Optionally filter by role")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List returned"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun findAll(
        @Parameter(description = "Filter by role (e.g. TRAINER, NUTRITIONIST)")
        @RequestParam(required = false) role: String?
    ): List<SpecialistResponse> =
        if (!role.isNullOrBlank()) specialistService.findByRole(role)
        else specialistService.findAll()

    @GetMapping("/{id}")
    @Operation(summary = "Get specialist by ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Specialist found"),
        ApiResponse(responseCode = "404", description = "Specialist not found"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun findById(
        @Parameter(description = "Specialist ID") @PathVariable id: Long
    ): SpecialistResponse =
        specialistService.findById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new specialist")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Specialist created"),
        ApiResponse(responseCode = "400", description = "Validation error"),
        ApiResponse(responseCode = "409", description = "Email already exists"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun create(@Valid @RequestBody request: SpecialistRequest): SpecialistResponse =
        specialistService.create(request)

    @PutMapping("/{id}")
    @Operation(summary = "Update specialist information")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Specialist updated"),
        ApiResponse(responseCode = "400", description = "Validation error"),
        ApiResponse(responseCode = "404", description = "Specialist not found"),
        ApiResponse(responseCode = "409", description = "Email already exists"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun update(
        @Parameter(description = "Specialist ID") @PathVariable id: Long,
        @Valid @RequestBody request: SpecialistRequest
    ): SpecialistResponse =
        specialistService.update(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a specialist")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Specialist deleted"),
        ApiResponse(responseCode = "404", description = "Specialist not found"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun delete(
        @Parameter(description = "Specialist ID") @PathVariable id: Long
    ) = specialistService.delete(id)
}
