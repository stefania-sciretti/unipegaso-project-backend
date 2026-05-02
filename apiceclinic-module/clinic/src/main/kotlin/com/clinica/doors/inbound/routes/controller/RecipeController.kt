package com.clinica.controller

import com.clinica.application.service.RecipeService
import com.clinica.dto.RecipeRequest
import com.clinic.model.RecipeResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/recipes")
@Tag(name = "Recipes", description = "Fit recipe collection management")
class RecipeController(private val recipeService: RecipeService) {

    @GetMapping
    @Operation(summary = "List all recipes", description = "Optionally filter by category or search by title")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List returned"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun findAll(
        @RequestParam(required = false) category: String?,
        @RequestParam(required = false) search: String?
    ): List<RecipeResponse> = recipeService.findAll(category, search)

    @GetMapping("/{id}")
    @Operation(summary = "Get recipe by ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Recipe found"),
        ApiResponse(responseCode = "404", description = "Recipe not found"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun findById(@PathVariable id: Long): RecipeResponse =
        recipeService.findById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new recipe")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Recipe created"),
        ApiResponse(responseCode = "400", description = "Validation error"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun create(@Valid @RequestBody request: RecipeRequest): RecipeResponse =
        recipeService.create(request)

    @PutMapping("/{id}")
    @Operation(summary = "Update a recipe")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Recipe updated"),
        ApiResponse(responseCode = "400", description = "Validation error"),
        ApiResponse(responseCode = "404", description = "Recipe not found"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun update(@PathVariable id: Long, @Valid @RequestBody request: RecipeRequest): RecipeResponse =
        recipeService.update(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a recipe")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Recipe deleted"),
        ApiResponse(responseCode = "404", description = "Recipe not found"),
        ApiResponse(responseCode = "401", description = "Unauthorized")
    ])
    fun delete(@PathVariable id: Long) = recipeService.delete(id)
}
