package com.clinica.doors.outbound.database.dao

import com.clinica.application.domain.Recipe
import com.clinica.doors.outbound.database.mappers.toDomain
import com.clinica.doors.outbound.database.mappers.toEntity
import com.clinica.doors.outbound.database.repositories.RecipeRepository
import org.springframework.stereotype.Component

@Component
class RecipeDao(
    private val repository: RecipeRepository
) {

    fun findAll(category: String?, search: String?): List<Recipe> =
        repository.search(category, search).map { it.toDomain() }

    fun findById(id: Long): Recipe? =
        repository.findById(id).orElse(null)?.toDomain()

    fun save(recipe: Recipe): Recipe =
        repository.save(recipe.toEntity()).toDomain()

    fun deleteById(id: Long) =
        repository.deleteById(id)
}