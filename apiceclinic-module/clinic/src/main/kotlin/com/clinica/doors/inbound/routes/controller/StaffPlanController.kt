package com.clinica.controller

import com.clinica.application.service.TrainingPlanServicePort
import com.clinica.dto.TrainingPlanRequest
import com.clinica.dto.TrainingPlanResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/training-plans")
@Tag(name = "Training Plans", description = "Personalized workout plans management")
class StaffPlanController(private val trainingPlanService: TrainingPlanServicePort) {

    @GetMapping
    @Operation(summary = "List all training plans", description = "Optionally filter by clientId")
    fun findAll(@RequestParam(required = false) clientId: Long?): List<TrainingPlanResponse> =
        trainingPlanService.findAll(clientId)

    @GetMapping("/{id}")
    @Operation(summary = "Get training plan by ID")
    fun findById(@PathVariable id: Long): TrainingPlanResponse =
        trainingPlanService.findById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new training plan")
    fun create(@Valid @RequestBody request: TrainingPlanRequest): TrainingPlanResponse =
        trainingPlanService.create(request)

    @PutMapping("/{id}")
    @Operation(summary = "Update a training plan")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: TrainingPlanRequest): TrainingPlanResponse =
        trainingPlanService.update(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a training plan")
    fun delete(@PathVariable id: Long) = trainingPlanService.delete(id)
}
