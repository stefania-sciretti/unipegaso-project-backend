package com.clinica.doors.inbound.routes.controller

import com.clinic.model.ReportRequest
import com.clinic.model.ReportResponse
import com.clinica.application.service.ReportService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
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
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List returned"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun findAll(): List<ReportResponse> =
        reportService.findAll()

    @GetMapping("/{id}")
    @Operation(summary = "Get report by ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Report found"),
        ApiResponse(responseCode = "404", description = "Report not found"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun findById(@PathVariable id: Long): ReportResponse =
        reportService.findById(id)

    @GetMapping("/appointment/{appointmentId}")
    @Operation(summary = "Get report by appointment ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Report found"),
        ApiResponse(responseCode = "404", description = "Report not found"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun findByAppointmentId(@PathVariable appointmentId: Long): ReportResponse =
        reportService.findByAppointmentId(appointmentId)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new report for a COMPLETED appointment")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Report created"),
        ApiResponse(responseCode = "400", description = "Validation error"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun create(@Valid @RequestBody request: ReportRequest): ReportResponse =
        reportService.create(request)

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing report")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Report updated"),
        ApiResponse(responseCode = "400", description = "Validation error"),
        ApiResponse(responseCode = "404", description = "Report not found"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: ReportRequest
    ): ReportResponse =
        reportService.update(id, request)
}
