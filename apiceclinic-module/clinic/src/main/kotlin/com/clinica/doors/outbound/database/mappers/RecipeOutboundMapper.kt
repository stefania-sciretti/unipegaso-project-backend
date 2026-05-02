package com.clinica.doors.outbound.database.mappers

import com.clinica.application.domain.Recipe
import com.clinica.doors.outbound.database.entities.RecipeEntity

fun RecipeEntity.toDomain(): Recipe =
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

fun Recipe.toEntity(): RecipeEntity =
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
