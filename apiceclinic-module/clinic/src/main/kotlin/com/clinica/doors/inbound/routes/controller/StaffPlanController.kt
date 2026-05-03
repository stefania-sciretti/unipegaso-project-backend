package com.clinica.controller

import com.clinica.application.service.TrainingPlanServicePort
import com.clinic.model.TrainingPlanRequest
import com.clinic.model.TrainingPlanResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/training-plans")
@Tag(name = "Training Plans", description = "Personalized workout plans management")
class StaffPlanController(private val trainingPlanService: TrainingPlanServicePort) {

    @GetMapping
    @Operation(summary = "List all training plans", description = "Optionally filter by patientId")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List returned"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun findAll(@RequestParam(required = false) patientId: Long?): List<TrainingPlanResponse> =
        trainingPlanService.findAll(patientId)

    @GetMapping("/{id}")
    @Operation(summary = "Get training plan by ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Training plan found"),
        ApiResponse(responseCode = "404", description = "Training plan not found"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun findById(@PathVariable id: Long): TrainingPlanResponse =
        trainingPlanService.findById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new training plan")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Training plan created"),
        ApiResponse(responseCode = "400", description = "Validation error"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun create(@Valid @RequestBody request: TrainingPlanRequest): TrainingPlanResponse =
        trainingPlanService.create(request)

    @PutMapping("/{id}")
    @Operation(summary = "Update a training plan")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Training plan updated"),
        ApiResponse(responseCode = "400", description = "Validation error"),
        ApiResponse(responseCode = "404", description = "Training plan not found"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun update(@PathVariable id: Long, @Valid @RequestBody request: TrainingPlanRequest): TrainingPlanResponse =
        trainingPlanService.update(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a training plan")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Training plan deleted"),
        ApiResponse(responseCode = "404", description = "Training plan not found"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun delete(@PathVariable id: Long) = trainingPlanService.delete(id)
}
