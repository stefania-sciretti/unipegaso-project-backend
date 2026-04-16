package com.clinica.service.api

import com.clinica.dto.RecipeRequest
import com.clinica.dto.RecipeResponse

/**
 * Contract for recipe management operations.
 */
interface RecipeServicePort {
    fun findAll(category: String?, search: String?): List<RecipeResponse>
    fun findById(id: Long): RecipeResponse
    fun create(request: RecipeRequest): RecipeResponse
    fun update(id: Long, request: RecipeRequest): RecipeResponse
    fun delete(id: Long)
}
