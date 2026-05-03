package com.clinica.application.service

import com.clinic.model.DietPlanRequest
import com.clinica.application.domain.DietPlan
import com.clinica.application.domain.Patient
import com.clinica.application.domain.Specialist
import com.clinica.doors.outbound.database.dao.DietPlanDao
import com.clinica.doors.outbound.database.dao.PatientDao
import com.clinica.doors.outbound.database.dao.SpecialistDao
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
import java.time.LocalDate
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class DietPlanServiceTest {

    @MockK private lateinit var dietPlanDao: DietPlanDao
    @MockK private lateinit var patientDao: PatientDao
    @MockK private lateinit var specialistDao: SpecialistDao

    @InjectMockKs
    private lateinit var service: DietPlanService

    private val now = LocalDateTime.now()

    private fun buildPatient(id: Long = 1L) = Patient(
        id = id, firstName = "Mario", lastName = "Rossi",
        fiscalCode = "RSSMRA80A01H501U", birthDate = LocalDate.of(1980, 1, 1),
        email = "mario@example.com"
    )

    private fun buildSpecialist(id: Long = 10L) = Specialist(
        id = id, firstName = "Anna", lastName = "Verdi",
        role = "NUTRITIONIST", email = "anna@example.com"
    )

    private fun buildDietPlan(id: Long = 1L) = DietPlan(
        id = id, patient = buildPatient(), specialist = buildSpecialist(),
        title = "Low-carb plan", description = "Lose 5kg",
        calories = 1800, durationWeeks = 8, active = true,
        createdAt = now, updatedAt = now
    )

    private fun buildRequest() = DietPlanRequest(
        patientId = 1L, specialistId = 10L, title = "Low-carb plan",
        description = "Lose 5kg", calories = 1800, durationWeeks = 8, active = true
    )

    @Test
    fun `findAll returns all plans when patientId is null`() {
        every { dietPlanDao.findAll(null) } returns listOf(buildDietPlan(1L), buildDietPlan(2L))
        assertEquals(2, service.findAll(null).size)
    }

    @Test
    fun `findAll filters by patientId`() {
        every { dietPlanDao.findAll(1L) } returns listOf(buildDietPlan())

        val result = service.findAll(1L)

        assertEquals(1, result.size)
        assertEquals(1L, result[0].patient.id)
    }

    @Test
    fun `findById returns diet plan response`() {
        every { dietPlanDao.findById(1L) } returns buildDietPlan()

        val result = service.findById(1L)

        assertEquals(1L, result.id)
        assertEquals("Low-carb plan", result.title)
        assertEquals("Mario", result.patient.firstName)
        assertEquals("Anna", result.specialist.firstName)
    }

    @Test
    fun `findById throws NoSuchElementException when not found`() {
        every { dietPlanDao.findById(99L) } returns null

        val ex = assertThrows<NoSuchElementException> { service.findById(99L) }
        assert(ex.message!!.contains("99"))
    }

    @Test
    fun `create saves and returns new diet plan`() {
        val saved = buildDietPlan(id = 5L)
        every { patientDao.findById(1L) } returns buildPatient()
        every { specialistDao.findById(10L) } returns buildSpecialist()
        every { dietPlanDao.save(any()) } returns saved

        val result = service.create(buildRequest())

        assertEquals(5L, result.id)
        assertEquals("Low-carb plan", result.title)
        verify { dietPlanDao.save(any()) }
    }

    @Test
    fun `create throws NoSuchElementException when client not found`() {
        every { patientDao.findById(1L) } returns null

        assertThrows<NoSuchElementException> { service.create(buildRequest()) }
        verify(exactly = 0) { dietPlanDao.save(any()) }
    }

    @Test
    fun `create throws NoSuchElementException when trainer not found`() {
        every { patientDao.findById(1L) } returns buildPatient()
        every { specialistDao.findById(10L) } returns null

        assertThrows<NoSuchElementException> { service.create(buildRequest()) }
        verify(exactly = 0) { dietPlanDao.save(any()) }
    }

    @Test
    fun `update saves and returns updated diet plan`() {
        val existing = buildDietPlan(1L)
        val updated = existing.copy(title = "Updated plan")
        every { dietPlanDao.findById(1L) } returns existing
        every { patientDao.findById(1L) } returns buildPatient()
        every { specialistDao.findById(10L) } returns buildSpecialist()
        every { dietPlanDao.save(any()) } returns updated

        val result = service.update(1L, buildRequest())

        assertEquals("Updated plan", result.title)
        verify { dietPlanDao.save(any()) }
    }

    @Test
    fun `update throws NoSuchElementException when diet plan not found`() {
        every { dietPlanDao.findById(99L) } returns null

        assertThrows<NoSuchElementException> { service.update(99L, buildRequest()) }
        verify(exactly = 0) { dietPlanDao.save(any()) }
    }

    @Test
    fun `update throws NoSuchElementException when client not found`() {
        every { dietPlanDao.findById(1L) } returns buildDietPlan()
        every { patientDao.findById(1L) } returns null

        assertThrows<NoSuchElementException> { service.update(1L, buildRequest()) }
        verify(exactly = 0) { dietPlanDao.save(any()) }
    }

    @Test
    fun `delete calls deleteById when diet plan exists`() {
        every { dietPlanDao.findById(1L) } returns buildDietPlan()
        every { dietPlanDao.deleteById(1L) } just runs

        service.delete(1L)

        verify { dietPlanDao.deleteById(1L) }
    }

    @Test
    fun `delete throws NoSuchElementException when diet plan not found`() {
        every { dietPlanDao.findById(99L) } returns null

        assertThrows<NoSuchElementException> { service.delete(99L) }
        verify(exactly = 0) { dietPlanDao.deleteById(any()) }
    }
}
