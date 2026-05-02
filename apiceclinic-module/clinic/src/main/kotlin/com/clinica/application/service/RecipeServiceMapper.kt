package com.clinica.application.service

import com.clinica.application.domain.Recipe
import com.clinica.dto.RecipeResponse

fun Recipe.toResponse(): RecipeResponse =
    RecipeResponse(
        id = id,
        title = title,
        description = description,
        ingredients = ingredients,
        instructions = instructions,
        calories = calories,
        category = category,
        createdAt = createdAt
    )
