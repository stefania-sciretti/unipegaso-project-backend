package com.clinica.doors.inbound.routes.controller

import com.clinica.application.service.PatientService
import com.clinica.doors.inbound.routes.mappers.toResponse
import com.clinic.model.PatientRequest
import com.clinic.model.PatientResponse
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Size
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("/api/patients")
@Tag(name = "Patients", description = "Patient management")
class PatientController(
    private val patientService: PatientService,
) {

    @GetMapping
    @Operation(summary = "List all patients, optionally filtered by name")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List returned"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun getAllPatients(
        @Parameter(description = "Filter by first or last name (case-insensitive, minimum 3 characters)")
        @RequestParam(required = false) @Size(min = 3, message = "Search query must be at least 3 characters") search: String?
    ): List<PatientResponse> {
        val patients =
            if (!search.isNullOrBlank()) patientService.search(search)
            else patientService.getAllPatients()
        return patients.map { it.toResponse() }
    }

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

    //TODO("se rimane tempo implementiamo, altrimenti ciao")
    @GetMapping("/{id}")
    @Operation(summary = "Get patient by ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Patient found"),
        ApiResponse(responseCode = "404", description = "Patient not found"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun findById(@PathVariable id: Long): PatientResponse =
        patientService.findById(id).toResponse()

    @PutMapping("/{id}")
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
