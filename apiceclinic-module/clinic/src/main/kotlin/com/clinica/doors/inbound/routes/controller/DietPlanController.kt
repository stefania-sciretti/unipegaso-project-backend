package com.clinica.controller

import com.clinica.application.service.DietPlanServicePort
import com.clinica.dto.DietPlanRequest
import com.clinica.dto.DietPlanResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/diet-plans")
@Tag(name = "Diet Plans", description = "Personalized nutrition plans management")
class DietPlanController(private val dietPlanService: DietPlanServicePort) {

    @GetMapping
    @Operation(summary = "List all diet plans", description = "Optionally filter by clientId")
    fun findAll(@RequestParam(required = false) clientId: Long?): List<DietPlanResponse> =
        dietPlanService.findAll(clientId)

    @GetMapping("/{id}")
    @Operation(summary = "Get diet plan by ID")
    fun findById(@PathVariable id: Long): DietPlanResponse =
        dietPlanService.findById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new diet plan")
    fun create(@Valid @RequestBody request: DietPlanRequest): DietPlanResponse =
        dietPlanService.create(request)

    @PutMapping("/{id}")
    @Operation(summary = "Update a diet plan")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: DietPlanRequest): DietPlanResponse =
        dietPlanService.update(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a diet plan")
    fun delete(@PathVariable id: Long) = dietPlanService.delete(id)
}
