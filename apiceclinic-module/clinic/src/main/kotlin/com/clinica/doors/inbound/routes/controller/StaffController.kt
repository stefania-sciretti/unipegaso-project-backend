package com.clinica.controller

import com.clinica.application.service.StaffService
import com.clinica.dto.StaffResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/trainers")
class StaffController(private val staffService: StaffService) {

    @GetMapping
    @Operation(summary = "List all staff members", description = "Optionally filter by role")
    fun findAll(@RequestParam(required = false) role: String?): List<StaffResponse> =
        if (!role.isNullOrBlank()) staffService.findByRole(role)
        else staffService.findAll()

    @GetMapping("/{id}")
    @Operation(summary = "Get staff member by ID")
    fun findById(@PathVariable id: Long): StaffResponse =
        staffService.findById(id)
}
