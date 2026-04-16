package com.clinica.application.service

import com.clinica.application.domain.Recipe
import com.clinica.doors.outbound.database.dao.RecipeDao
import com.clinica.dto.RecipeRequest
import com.clinica.dto.RecipeResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class RecipeService(
    private val recipeDao: RecipeDao
) {

    @Transactional(readOnly = true)
    fun findAll(category: String?, search: String?): List<RecipeResponse> =
        recipeDao.findAll(category, search).map { it.toResponse() }

    @Transactional(readOnly = true)
    fun findById(id: Long): RecipeResponse {
        val recipe = recipeDao.findById(id) ?: throw NoSuchElementException("Recipe $id not found")
        return recipe.toResponse()
    }

    @Transactional
    fun create(request: RecipeRequest): RecipeResponse {
        val now = LocalDateTime.now()
        val recipe = Recipe(
            id = 0,
            title = request.title,
            description = request.description,
            ingredients = request.ingredients,
            instructions = request.instructions,
            calories = request.calories,
            category = request.category,
            createdAt = now,
            updatedAt = now
        )
        return recipeDao.save(recipe).toResponse()
    }

    @Transactional
    fun update(id: Long, request: RecipeRequest): RecipeResponse {
        val existing = recipeDao.findById(id) ?: throw NoSuchElementException("Recipe $id not found")
        val updated = existing.copy(
            title = request.title,
            description = request.description,
            ingredients = request.ingredients,
            instructions = request.instructions,
            calories = request.calories,
            category = request.category,
            updatedAt = LocalDateTime.now()
        )
        return recipeDao.save(updated).toResponse()
    }

    @Transactional
    fun delete(id: Long) = recipeDao.deleteById(id)

    private fun Recipe.toResponse() = RecipeResponse(
        id = id,
        title = title,
        description = description,
        ingredients = ingredients,
        instructions = instructions,
        calories = calories,
        category = category,
        createdAt = createdAt
    )
}