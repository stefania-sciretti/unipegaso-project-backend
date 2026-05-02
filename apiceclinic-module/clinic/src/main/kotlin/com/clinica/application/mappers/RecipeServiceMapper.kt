package com.clinica.application.mappers

import com.clinic.model.RecipeResponse
import com.clinica.application.domain.Recipe
import java.time.ZoneOffset

fun Recipe.toResponse(): RecipeResponse =
    RecipeResponse(
        id = id,
        title = title,
        description = description,
        ingredients = ingredients,
        instructions = instructions,
        calories = calories,
        category = category,
        createdAt = createdAt.atOffset(ZoneOffset.UTC)
    )
