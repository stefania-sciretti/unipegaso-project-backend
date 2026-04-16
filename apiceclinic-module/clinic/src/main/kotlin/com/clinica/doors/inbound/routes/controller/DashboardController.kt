package com.clinica.controller

import com.clinica.application.service.DashboardService
import com.clinica.dto.DashboardResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Dashboard statistics")
class DashboardController(private val dashboardService: DashboardService) {

    @GetMapping
    @Operation(summary = "Get dashboard statistics")
    fun getDashboard(): DashboardResponse = dashboardService.getDashboard()
}
