package com.clinica.application.service

import com.clinica.application.domain.Specialist
import com.clinica.doors.outbound.database.dao.SpecialistDao
import com.clinica.dto.SpecialistRequest
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class SpecialistServiceTest {

    @MockK
    private lateinit var specialistDao: SpecialistDao

    @InjectMockKs
    private lateinit var service: SpecialistService

    private fun buildSpecialist(id: Long = 1L, role: String = "NUTRITIONIST") = Specialist(
        id = id, firstName = "Anna", lastName = "Verdi",
        role = role, bio = "Specialist", email = "anna@example.com",
        createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now()
    )

    // findAll

    @Test
    fun `findAll returns all specialists`() {
        every { specialistDao.findAll() } returns listOf(buildSpecialist(1L), buildSpecialist(2L, "TRAINER"))

        val result = service.findAll()

        assertEquals(2, result.size)
        assertEquals("NUTRITIONIST", result[0].role)
        assertEquals("TRAINER", result[1].role)
    }

    @Test
    fun `findAll returns empty list when no specialists`() {
        every { specialistDao.findAll() } returns emptyList()
        assertEquals(0, service.findAll().size)
    }

    // findByRole

    @Test
    fun `findByRole returns specialists matching role`() {
        every { specialistDao.findByRole("TRAINER") } returns listOf(buildSpecialist(2L, "TRAINER"))

        val result = service.findByRole("TRAINER")

        assertEquals(1, result.size)
        assertEquals("TRAINER", result[0].role)
    }

    @Test
    fun `findByRole returns empty list when no match`() {
        every { specialistDao.findByRole("UNKNOWN") } returns emptyList()
        assertEquals(0, service.findByRole("UNKNOWN").size)
    }

    // findById

    @Test
    fun `findById returns specialist response`() {
        every { specialistDao.findById(1L) } returns buildSpecialist()

        val result = service.findById(1L)

        assertEquals(1L, result.id)
        assertEquals("Anna", result.firstName)
        assertEquals("NUTRITIONIST", result.role)
    }

    @Test
    fun `findById throws NoSuchElementException when not found`() {
        every { specialistDao.findById(99L) } returns null

        val ex = assertThrows<NoSuchElementException> { service.findById(99L) }
        assert(ex.message!!.contains("99"))
    }

    // create

    @Test
    fun `create saves and returns specialist response`() {
        val request = SpecialistRequest(
            firstName = "Mario", lastName = "Bianchi",
            role = "TRAINER", bio = null, email = "mario@example.com"
        )
        val saved = buildSpecialist(id = 5L, role = "TRAINER")
        every { specialistDao.existsByEmail("mario@example.com") } returns false
        every { specialistDao.save(any()) } returns saved

        val result = service.create(request)

        assertEquals(5L, result.id)
        assertEquals("TRAINER", result.role)
    }

    @Test
    fun `create throws when email already exists`() {
        val request = SpecialistRequest(
            firstName = "Mario", lastName = "Bianchi",
            role = "TRAINER", bio = null, email = "anna@example.com"
        )
        every { specialistDao.existsByEmail("anna@example.com") } returns true

        assertThrows<IllegalStateException> { service.create(request) }
    }

    // update

    @Test
    fun `update returns updated specialist`() {
        val request = SpecialistRequest(
            firstName = "Anna", lastName = "Verdi",
            role = "NUTRITIONIST", bio = "Updated bio", email = "anna@example.com"
        )
        val existing = buildSpecialist(1L)
        val updated = existing.copy(bio = "Updated bio")
        every { specialistDao.findById(1L) } returns existing
        every { specialistDao.existsByEmailAndIdNot("anna@example.com", 1L) } returns false
        every { specialistDao.save(any()) } returns updated

        val result = service.update(1L, request)
        assertEquals(1L, result.id)
    }

    @Test
    fun `update throws when not found`() {
        val request = SpecialistRequest("A", "B", "TRAINER", null, "x@x.com")
        every { specialistDao.findById(99L) } returns null

        assertThrows<NoSuchElementException> { service.update(99L, request) }
    }

    @Test
    fun `update throws when email already taken by another specialist`() {
        val request = SpecialistRequest("A", "B", "TRAINER", null, "other@example.com")
        every { specialistDao.findById(1L) } returns buildSpecialist(1L)
        every { specialistDao.existsByEmailAndIdNot("other@example.com", 1L) } returns true

        assertThrows<IllegalArgumentException> { service.update(1L, request) }
    }

    // delete

    @Test
    fun `delete removes specialist`() {
        every { specialistDao.findById(1L) } returns buildSpecialist(1L)
        every { specialistDao.deleteById(1L) } returns Unit

        service.delete(1L)

        io.mockk.verify(exactly = 1) { specialistDao.deleteById(1L) }
    }

    @Test
    fun `delete throws when specialist not found`() {
        every { specialistDao.findById(99L) } returns null

        assertThrows<NoSuchElementException> { service.delete(99L) }
    }
}

