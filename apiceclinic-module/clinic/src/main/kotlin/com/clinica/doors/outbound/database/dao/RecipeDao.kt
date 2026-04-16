package com.clinica.doors.outbound.database.dao

import com.clinica.application.domain.Recipe
import com.clinica.doors.outbound.database.entities.RecipeEntity
import com.clinica.doors.outbound.database.repositories.RecipeRepository
import org.springframework.stereotype.Component

@Component
class RecipeDao(
    private val repository: RecipeRepository
)  {

    fun findAll(category: String?, search: String?): List<Recipe> =
        repository.search(category, search).map { it.toDomain() }

    fun findById(id: Long): Recipe? =
        repository.findById(id).orElse(null)?.toDomain()

    fun save(recipe: Recipe): Recipe {
        val entity = recipe.toEntity()
        val saved = repository.save(entity)
        return saved.toDomain()
    }

    fun deleteById(id: Long) {
        repository.deleteById(id)
    }

    private fun RecipeEntity.toDomain(): Recipe =
        Recipe(
            id = this.id,
            title = this.title,
            description = this.description,
            ingredients = this.ingredients,
            instructions = this.instructions,
            calories = this.calories,
            category = this.category,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )

    private fun Recipe.toEntity(): RecipeEntity =
        RecipeEntity(
            id = this.id,
            title = this.title,
            description = this.description,
            ingredients = this.ingredients,
            instructions = this.instructions,
            calories = this.calories,
            category = this.category,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
}