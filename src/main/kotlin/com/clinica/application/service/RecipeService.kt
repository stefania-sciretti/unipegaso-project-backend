package com.clinica.service

import com.clinica.domain.RecipeEntity
import com.clinica.dto.RecipeRequest
import com.clinica.dto.RecipeResponse
import com.clinica.repository.RecipeRepository
import com.clinica.service.api.RecipeServicePort
import com.clinica.util.orEntityNotFound
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RecipeService(private val recipeRepository: RecipeRepository) : RecipeServicePort {

    @Transactional(readOnly = true)
    override fun findAll(category: String?, search: String?): List<RecipeResponse> {
        val recipes = when {
            !category.isNullOrBlank() -> recipeRepository.findByCategoryIgnoreCase(category)
            !search.isNullOrBlank()   -> recipeRepository.findByTitleContainingIgnoreCase(search)
            else                      -> recipeRepository.findAll()
        }
        return recipes.map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): RecipeResponse =
        recipeRepository.findById(id)?.toResponse() ?: throw NoSuchElementException("Recipe not found with id: $id")

    override fun create(request: RecipeRequest): RecipeResponse {
        val recipe = RecipeEntity(
            title = request.title,
            description = request.description,
            ingredients = request.ingredients,
            instructions = request.instructions,
            calories = request.calories,
            category = request.category
        )
        return recipeRepository.save(recipe).toResponse()
    }

    override fun update(id: Long, request: RecipeRequest): RecipeResponse {
        val recipe = recipeRepository.findById(id) ?: throw NoSuchElementException("Recipe not found with id: $id")

        recipe.title = request.title
        recipe.description = request.description
        recipe.ingredients = request.ingredients
        recipe.instructions = request.instructions
        recipe.calories = request.calories
        recipe.category = request.category

        return recipeRepository.save(recipe).toResponse()
    }

    override fun delete(id: Long) {
        if (!recipeRepository.existsById(id)) {
            throw NoSuchElementException("Recipe not found with id: $id")
        }
        recipeRepository.deleteById(id)
    }

    private fun RecipeEntity.toResponse() = RecipeResponse(
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
