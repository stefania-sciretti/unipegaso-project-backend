package com.clinica.controller

import com.clinica.dto.PatientRequest
import com.clinica.dto.PatientResponse
import com.clinica.service.PatientService
import io.swagger.v3.oas.annotations.Operation
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
    fun findAll(@RequestParam(required = false) search: String?): List<PatientResponse> =
        if (!search.isNullOrBlank()) patientService.search(search)
        else patientService.findAll()

    @GetMapping("/{id}")
    @Operation(summary = "Get patient by ID")
    fun findById(@PathVariable id: Long): PatientResponse =
        patientService.findById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new patient")
    fun create(@Valid @RequestBody request: PatientRequest): PatientResponse =
        patientService.create(request)

    @PutMapping("/{id}")
    @Operation(summary = "Update patient information")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: PatientRequest): PatientResponse =
        patientService.update(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a patient")
    fun delete(@PathVariable id: Long) = patientService.delete(id)
}
