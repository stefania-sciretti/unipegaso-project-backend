package com.clinica.controller

import com.clinica.dto.GlycemiaMeasurementRequest
import com.clinica.dto.GlycemiaMeasurementResponse
import com.clinica.service.api.GlycemiaMeasurementServicePort
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/glycemia-measurements")
@Tag(name = "Glycemia Measurements", description = "Gestione misurazioni glicemia tramite pungidito")
class GlycemiaMeasurementController(private val glycemiaService: GlycemiaMeasurementServicePort) {

    @GetMapping
    @Operation(summary = "Elenco misurazioni glicemia", description = "Filtrabile per clientId, ordinate per data decrescente")
    fun findAll(@RequestParam(required = false) clientId: Long?): List<GlycemiaMeasurementResponse> =
        glycemiaService.findAll(clientId)

    @GetMapping("/{id}")
    @Operation(summary = "Dettaglio misurazione glicemia per ID")
    fun findById(@PathVariable id: Long): GlycemiaMeasurementResponse =
        glycemiaService.findById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registra nuova misurazione glicemia")
    fun create(@Valid @RequestBody request: GlycemiaMeasurementRequest): GlycemiaMeasurementResponse =
        glycemiaService.create(request)

    @PutMapping("/{id}")
    @Operation(summary = "Aggiorna misurazione glicemia")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: GlycemiaMeasurementRequest
    ): GlycemiaMeasurementResponse =
        glycemiaService.update(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Elimina misurazione glicemia")
    fun delete(@PathVariable id: Long) = glycemiaService.delete(id)
}
