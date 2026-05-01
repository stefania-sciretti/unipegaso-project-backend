package com.clinica.application.service

import com.clinica.application.domain.Specialist
import com.clinica.doors.outbound.database.dao.SpecialistDao
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
}
