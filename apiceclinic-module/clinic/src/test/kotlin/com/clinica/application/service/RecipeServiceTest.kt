package com.clinica.application.service

import com.clinic.model.RecipeRequest
import com.clinica.application.domain.Recipe
import com.clinica.doors.outbound.database.dao.RecipeDao
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class RecipeServiceTest {

    @MockK private lateinit var recipeDao: RecipeDao

    @InjectMockKs
    private lateinit var service: RecipeService

    private val now = LocalDateTime.of(2026, 1, 15, 10, 0)

    private fun buildRecipe(id: Long = 1L, category: String? = "Lunch") = Recipe(
        id = id,
        title = "Pasta al Pomodoro",
        description = "Classic Italian pasta",
        ingredients = "Pasta, tomatoes, basil",
        instructions = "Boil pasta, add sauce",
        calories = 450,
        category = category,
        createdAt = now,
        updatedAt = now
    )

    private fun buildRequest(title: String = "Pasta al Pomodoro") = RecipeRequest(
        title = title,
        description = "Classic Italian pasta",
        ingredients = "Pasta, tomatoes, basil",
        instructions = "Boil pasta, add sauce",
        calories = 450,
        category = "Lunch"
    )

    @Test
    fun `findAll returns all recipes when no filters`() {
        every { recipeDao.findAll(null, null) } returns listOf(buildRecipe(1L), buildRecipe(2L))

        val result = service.findAll(null, null)

        assertEquals(2, result.size)
        verify { recipeDao.findAll(null, null) }
    }

    @Test
    fun `findAll filters by category`() {
        every { recipeDao.findAll("Lunch", null) } returns listOf(buildRecipe())

        val result = service.findAll("Lunch", null)

        assertEquals(1, result.size)
        assertEquals("Lunch", result[0].category)
    }

    @Test
    fun `findAll filters by search term`() {
        every { recipeDao.findAll(null, "Pasta") } returns listOf(buildRecipe())

        val result = service.findAll(null, "Pasta")

        assertEquals(1, result.size)
        assertEquals("Pasta al Pomodoro", result[0].title)
    }

    @Test
    fun `findById returns recipe response when found`() {
        every { recipeDao.findById(1L) } returns buildRecipe()

        val result = service.findById(1L)

        assertEquals(1L, result.id)
        assertEquals("Pasta al Pomodoro", result.title)
        assertEquals(450, result.calories)
        assertEquals("Lunch", result.category)
    }

    @Test
    fun `findById throws NoSuchElementException when not found`() {
        every { recipeDao.findById(99L) } returns null

        val ex = assertThrows<NoSuchElementException> { service.findById(99L) }
        assert(ex.message!!.contains("99"))
    }
    @Test
    fun `create saves and returns new recipe`() {
        val saved = buildRecipe(id = 5L)
        every { recipeDao.save(any()) } returns saved

        val result = service.create(buildRequest())

        assertEquals(5L, result.id)
        assertEquals("Pasta al Pomodoro", result.title)
        verify { recipeDao.save(any()) }
    }

    @Test
    fun `update saves and returns updated recipe`() {
        val existing = buildRecipe(1L)
        val updated = existing.copy(title = "Pasta Aglio e Olio")
        every { recipeDao.findById(1L) } returns existing
        every { recipeDao.save(any()) } returns updated

        val result = service.update(1L, buildRequest("Pasta Aglio e Olio"))

        assertEquals("Pasta Aglio e Olio", result.title)
        verify { recipeDao.save(any()) }
    }

    @Test
    fun `update throws NoSuchElementException when recipe not found`() {
        every { recipeDao.findById(99L) } returns null

        assertThrows<NoSuchElementException> { service.update(99L, buildRequest()) }
        verify(exactly = 0) { recipeDao.save(any()) }
    }

    @Test
    fun `delete calls deleteById`() {
        every { recipeDao.deleteById(1L) } just runs

        service.delete(1L)

        verify { recipeDao.deleteById(1L) }
    }
}
