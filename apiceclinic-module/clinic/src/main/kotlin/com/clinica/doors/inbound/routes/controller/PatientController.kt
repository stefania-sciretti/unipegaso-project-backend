package com.clinica.controller

import com.clinica.application.service.PatientService
import com.clinica.doors.inbound.routes.mappers.toResponse
import com.clinica.dto.PatientRequest
import com.clinica.dto.PatientResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/patients")
@Tag(name = "Patients", description = "Patient management")
class PatientController(
    private val patientService: PatientService,
) {

    @GetMapping
    @Operation(summary = "List all patients")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List returned"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun getAllPatients(): List<PatientResponse> =
        patientService.getAllPatients().map { it.toResponse() }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new patient")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Patient created"),
        ApiResponse(responseCode = "400", description = "Validation error"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun create(@Valid @RequestBody request: PatientRequest): PatientResponse =
        patientService.create(request).toResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get patient by ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Patient found"),
        ApiResponse(responseCode = "404", description = "Patient not found"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun findById(@PathVariable id: Long): PatientResponse =
        patientService.findById(id).toResponse()

    @PatchMapping("/{id}")
    @Operation(summary = "Update patient information")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Patient updated"),
        ApiResponse(responseCode = "400", description = "Validation error"),
        ApiResponse(responseCode = "404", description = "Patient not found"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun update(@PathVariable id: Long, @Valid @RequestBody request: PatientRequest): PatientResponse =
        patientService.update(id, request).toResponse()

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a patient")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Patient deleted"),
        ApiResponse(responseCode = "404", description = "Patient not found"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun delete(@PathVariable id: Long) = patientService.delete(id)
}
