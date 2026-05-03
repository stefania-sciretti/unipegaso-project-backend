package com.clinica.doors.inbound.routes.controller

import com.clinica.application.service.DietPlanService
import com.clinica.application.mappers.toResponse
import com.clinic.model.DietPlanRequest
import com.clinic.model.DietPlanResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/diet-plans")
@Tag(name = "Diet Plans", description = "Personalized nutrition plans management")
class DietPlanController(private val dietPlanService: DietPlanService) {

    @GetMapping
    @Operation(summary = "List all diet plans", description = "Optionally filter by patientId")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List returned"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun findAll(@RequestParam(required = false) patientId: Long?): List<DietPlanResponse> =
        dietPlanService.findAll(patientId).map { it.toResponse() }

    @GetMapping("/{id}")
    @Operation(summary = "Get diet plan by ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Diet plan found"),
        ApiResponse(responseCode = "404", description = "Diet plan not found"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun findById(@PathVariable id: Long): DietPlanResponse =
        dietPlanService.findById(id).toResponse()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new diet plan")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Diet plan created"),
        ApiResponse(responseCode = "400", description = "Validation error"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun create(@Valid @RequestBody request: DietPlanRequest): DietPlanResponse =
        dietPlanService.create(request).toResponse()

    @PutMapping("/{id}")
    @Operation(summary = "Update a diet plan")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Diet plan updated"),
        ApiResponse(responseCode = "400", description = "Validation error"),
        ApiResponse(responseCode = "404", description = "Diet plan not found"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun update(@PathVariable id: Long, @Valid @RequestBody request: DietPlanRequest): DietPlanResponse =
        dietPlanService.update(id, request).toResponse()

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a diet plan")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Diet plan deleted"),
        ApiResponse(responseCode = "404", description = "Diet plan not found"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun delete(@PathVariable id: Long) = dietPlanService.delete(id)
}
