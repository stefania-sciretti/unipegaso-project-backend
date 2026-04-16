package com.clinica.controller

import com.clinica.application.service.PatientService
import com.clinica.doors.inbound.routes.mappers.toResponse
import com.clinica.dto.PatientRequest
import com.clinica.dto.PatientResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/patients")
@Tag(name = "Patients", description = "Patient management")
class PatientController(
    private val patientService: PatientService,
) {

    @GetMapping
    @Operation(summary = "List all patients")
    fun getAllPatients(): List<PatientResponse> =
        patientService.getAllPatients().map { it.toResponse() }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new patient")
    fun create(@Valid @RequestBody request: PatientRequest): PatientResponse =
        patientService.create(request).toResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get patient by ID")
    fun findById(@PathVariable id: Long): PatientResponse =
        patientService.findById(id).toResponse()

    @PatchMapping("/{id}")
    @Operation(summary = "Update patient information")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: PatientRequest): PatientResponse =
        patientService.update(id, request).toResponse()

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a patient")
    fun delete(@PathVariable id: Long) = patientService.delete(id)
}
