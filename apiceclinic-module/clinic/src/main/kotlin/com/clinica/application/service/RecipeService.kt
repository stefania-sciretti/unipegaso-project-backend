package com.clinica.application.service

import com.clinica.application.domain.Recipe
import com.clinica.doors.outbound.database.dao.RecipeDao
import com.clinic.model.RecipeRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class RecipeService(
    private val recipeDao: RecipeDao
) {

    @Transactional(readOnly = true)
    fun findAll(category: String?, search: String?): List<Recipe> =
        recipeDao.findAll(category, search)

    @Transactional(readOnly = true)
    fun findById(id: Long): Recipe =
        recipeDao.findById(id).orThrow("Recipe $id not found")

    @Transactional
    fun create(request: RecipeRequest): Recipe {
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
        return recipeDao.save(recipe)
    }

    @Transactional
    fun update(id: Long, request: RecipeRequest): Recipe {
        val existing = recipeDao.findById(id).orThrow("Recipe $id not found")
        val updated = existing.copy(
            title = request.title,
            description = request.description,
            ingredients = request.ingredients,
            instructions = request.instructions,
            calories = request.calories,
            category = request.category,
            updatedAt = LocalDateTime.now()
        )
        return recipeDao.save(updated)
    }

    @Transactional
    fun delete(id: Long) = recipeDao.deleteById(id)
}