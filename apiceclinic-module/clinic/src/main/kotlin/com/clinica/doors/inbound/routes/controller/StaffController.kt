package com.clinica.controller

import com.clinica.application.service.SpecialistService
import com.clinica.dto.SpecialistResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/specialists")
class SpecialistController(private val specialistService: SpecialistService) {

    @GetMapping
    @Operation(summary = "List all specialists", description = "Optionally filter by role")
    fun findAll(@RequestParam(required = false) role: String?): List<SpecialistResponse> =
        if (!role.isNullOrBlank()) specialistService.findByRole(role)
        else specialistService.findAll()

    @GetMapping("/{id}")
    @Operation(summary = "Get specialist by ID")
    fun findById(@PathVariable id: Long): SpecialistResponse =
        specialistService.findById(id)
}
