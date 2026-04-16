package com.clinica.controller

import com.clinica.application.service.ReportService
import com.clinica.dto.ReportRequest
import com.clinica.dto.ReportResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports", description = "Clinical report management")
class ReportController(
    private val reportService: ReportService
) {

    @GetMapping
    @Operation(summary = "List all reports")
    fun findAll(): List<ReportResponse> =
        reportService.findAll()

    @GetMapping("/{id}")
    @Operation(summary = "Get report by ID")
    fun findById(@PathVariable id: Long): ReportResponse =
        reportService.findById(id)

    @GetMapping("/appointment/{appointmentId}")
    @Operation(summary = "Get report by appointment ID")
    fun findByAppointmentId(@PathVariable appointmentId: Long): ReportResponse =
        reportService.findByAppointmentId(appointmentId)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new report for a COMPLETED appointment")
    fun create(@Valid @RequestBody request: ReportRequest): ReportResponse =
        reportService.create(request)

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing report")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: ReportRequest
    ): ReportResponse =
        reportService.update(id, request)
}
