package com.clinica.controller

import com.clinica.dto.StaffResponse
import com.clinica.service.StaffService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/trainers")
@Tag(name = "Staff", description = "Staff members: nutritionists, personal trainers, sports doctors, osteopaths")
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
