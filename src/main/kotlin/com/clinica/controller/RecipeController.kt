package com.clinica.controller

import com.clinica.dto.RecipeRequest
import com.clinica.dto.RecipeResponse
import com.clinica.service.api.RecipeServicePort
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/recipes")
@Tag(name = "Recipes", description = "Fit recipe collection management")
class RecipeController(private val recipeService: RecipeServicePort) {

    @GetMapping
    @Operation(summary = "List all recipes", description = "Optionally filter by category or search by title")
    fun findAll(
        @RequestParam(required = false) category: String?,
        @RequestParam(required = false) search: String?
    ): List<RecipeResponse> = recipeService.findAll(category, search)

    @GetMapping("/{id}")
    @Operation(summary = "Get recipe by ID")
    fun findById(@PathVariable id: Long): RecipeResponse =
        recipeService.findById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new recipe")
    fun create(@Valid @RequestBody request: RecipeRequest): RecipeResponse =
        recipeService.create(request)

    @PutMapping("/{id}")
    @Operation(summary = "Update a recipe")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: RecipeRequest): RecipeResponse =
        recipeService.update(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a recipe")
    fun delete(@PathVariable id: Long) = recipeService.delete(id)
}
