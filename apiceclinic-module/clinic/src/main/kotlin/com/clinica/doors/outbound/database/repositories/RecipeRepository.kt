package com.clinica.doors.outbound.database.repositories

import com.clinica.doors.outbound.database.entities.RecipeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RecipeRepository : JpaRepository<RecipeEntity, Long> {

    fun findByCategory(category: String): List<RecipeEntity>

    fun findByTitleContainingIgnoreCase(title: String): List<RecipeEntity>

    fun findByCategoryAndTitleContainingIgnoreCase(
        category: String,
        title: String
    ): List<RecipeEntity>

    @Query(
        """
        SELECT r
        FROM RecipeEntity r
        WHERE (:category IS NULL OR r.category = :category)
          AND (:search   IS NULL OR LOWER(r.title) LIKE LOWER(CONCAT('%', :search, '%')))
        """
    )
    fun search(
        @Param("category") category: String?,
        @Param("search") search: String?
    ): List<RecipeEntity>
}