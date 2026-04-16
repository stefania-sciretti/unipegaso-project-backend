package com.clinica.repository

import com.clinica.domain.Recipe
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RecipeRepository : JpaRepository<Recipe, Long> {
    fun findByCategoryIgnoreCase(category: String): List<Recipe>
    fun findByTitleContainingIgnoreCase(title: String): List<Recipe>
}
