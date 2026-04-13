package com.clinica.controller

import com.clinica.dto.ClientRequest
import com.clinica.dto.ClientResponse
import com.clinica.service.api.ClientServicePort
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/clients")
@Tag(name = "Clients", description = "Fitness center client management")
class ClientController(private val clientService: ClientServicePort) {

    @GetMapping
    @Operation(summary = "List all clients", description = "Returns all clients, with optional search by name")
    fun findAll(@RequestParam(required = false) search: String?): List<ClientResponse> =
        if (!search.isNullOrBlank()) clientService.search(search)
        else clientService.findAll()

    @GetMapping("/{id}")
    @Operation(summary = "Get client by ID")
    fun findById(@PathVariable id: Long): ClientResponse =
        clientService.findById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new client")
    fun create(@Valid @RequestBody request: ClientRequest): ClientResponse =
        clientService.create(request)

    @PutMapping("/{id}")
    @Operation(summary = "Update client information")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: ClientRequest): ClientResponse =
        clientService.update(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a client")
    fun delete(@PathVariable id: Long) = clientService.delete(id)
}
