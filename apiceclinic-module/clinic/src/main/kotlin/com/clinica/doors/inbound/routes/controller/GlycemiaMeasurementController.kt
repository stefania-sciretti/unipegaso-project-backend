package com.clinica.doors.inbound.routes.controller

import com.clinica.application.service.GlycemiaMeasurementService
import com.clinica.dto.GlycemiaClassificationRulesResponse
import com.clinica.dto.GlycemiaContextRules
import com.clinica.dto.GlycemiaThreshold
import com.clinica.dto.GlycemiaMeasurementRequest
import com.clinica.dto.GlycemiaMeasurementResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/glycemia-measurements")
@Tag(name = "Glycemia Measurements", description = "Blood glucose monitoring — ADA/WHO classification")
class GlycemiaMeasurementController(
    private val glycemiaMeasurementService: GlycemiaMeasurementService
) {

    @GetMapping
    @Operation(
        summary = "List all glycemia measurements",
        description = "Returns all measurements ordered by date desc. Optionally filter by patientId."
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List returned"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun findAll(
        @Parameter(description = "Filter by patient ID")
        @RequestParam(required = false) patientId: Long?
    ): List<GlycemiaMeasurementResponse> =
        glycemiaMeasurementService.findAll(patientId)

    @GetMapping("/classification-rules")
    @Operation(
        summary = "Get glycemia classification rules",
        description = "Returns ADA/WHO thresholds for each context (A_DIGIUNO, POST_PASTO_1H, POST_PASTO_2H). Use this to dynamically render classification legends in the UI."
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Classification rules returned")
    ])
    fun getClassificationRules(): GlycemiaClassificationRulesResponse =
        GlycemiaClassificationRulesResponse(
            contexts = listOf(
                GlycemiaContextRules(
                    context = "A_DIGIUNO",
                    label = "A digiuno",
                    thresholds = listOf(
                        GlycemiaThreshold("NORMALE",    "< 100 MG/DL = NORMALE",   null, 99),
                        GlycemiaThreshold("ATTENZIONE", "100–125 = ATTENZIONE",    100,  125),
                        GlycemiaThreshold("ELEVATA",    "≥ 126 = ELEVATA",         126,  null)
                    )
                ),
                GlycemiaContextRules(
                    context = "POST_PASTO_1H",
                    label = "Post-pasto 1h",
                    thresholds = listOf(
                        GlycemiaThreshold("NORMALE",    "< 155 MG/DL = NORMALE",   null, 154),
                        GlycemiaThreshold("ATTENZIONE", "155–208 = ATTENZIONE",    155,  208),
                        GlycemiaThreshold("ELEVATA",    "≥ 209 = ELEVATA",         209,  null)
                    )
                ),
                GlycemiaContextRules(
                    context = "POST_PASTO_2H",
                    label = "Post-pasto 2h",
                    thresholds = listOf(
                        GlycemiaThreshold("NORMALE",    "< 140 MG/DL = NORMALE",   null, 139),
                        GlycemiaThreshold("ATTENZIONE", "140–199 = ATTENZIONE",    140,  199),
                        GlycemiaThreshold("ELEVATA",    "≥ 200 = ELEVATA",         200,  null)
                    )
                )
            )
        )

    @GetMapping("/{id}")
    @Operation(summary = "Get a glycemia measurement by ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Measurement found"),
        ApiResponse(responseCode = "404", description = "Measurement not found"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun findById(@PathVariable id: Long): GlycemiaMeasurementResponse =
        glycemiaMeasurementService.findById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Record a new glycemia measurement")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Measurement created"),
        ApiResponse(responseCode = "400", description = "Validation error"),
        ApiResponse(responseCode = "404", description = "Patient or specialist not found"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun create(@Valid @RequestBody request: GlycemiaMeasurementRequest): GlycemiaMeasurementResponse =
        glycemiaMeasurementService.create(request)

    @PutMapping("/{id}")
    @Operation(summary = "Update a glycemia measurement")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Measurement updated"),
        ApiResponse(responseCode = "400", description = "Validation error"),
        ApiResponse(responseCode = "404", description = "Measurement not found"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: GlycemiaMeasurementRequest
    ): GlycemiaMeasurementResponse =
        glycemiaMeasurementService.update(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a glycemia measurement")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Measurement deleted"),
        ApiResponse(responseCode = "404", description = "Measurement not found"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun delete(@PathVariable id: Long) =
        glycemiaMeasurementService.delete(id)
}
