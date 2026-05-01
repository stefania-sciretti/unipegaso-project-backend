package com.clinica.application.service

import com.clinica.application.domain.Patient
import com.clinica.application.domain.Specialist
import com.clinica.application.domain.TrainingPlan
import com.clinica.doors.outbound.database.dao.PatientDao
import com.clinica.doors.outbound.database.dao.SpecialistDao
import com.clinica.doors.outbound.database.dao.TrainingPlanDao
import com.clinica.dto.TrainingPlanRequest
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
class TrainingPlanServiceTest {

    @MockK private lateinit var trainingPlanDao: TrainingPlanDao
    @MockK private lateinit var patientDao: PatientDao
    @MockK private lateinit var specialistDao: SpecialistDao

    @InjectMockKs
    private lateinit var service: TrainingPlanService

    private fun buildPatient(id: Long = 1L) = Patient(
        id = id, firstName = "Mario", lastName = "Rossi",
        fiscalCode = "RSSMRA80A01H501U", birthDate = LocalDate.of(1980, 1, 1),
        email = "mario@example.com"
    )

    private fun buildSpecialist(id: Long = 10L) = Specialist(
        id = id, firstName = "Anna", lastName = "Verdi",
        role = "TRAINER", email = "anna@example.com"
    )

    private fun buildPlan(id: Long = 1L) = TrainingPlan(
        id = id, patient = buildPatient(), specialist = buildSpecialist(),
        title = "Strength Plan", description = "12-week strength",
        weeks = 12, sessionsPerWeek = 3, active = true,
        createdAt = LocalDateTime.now()
    )

    private fun buildRequest() = TrainingPlanRequest(
        patientId = 1L, specialistId = 10L, title = "Strength Plan",
        description = "12-week strength", weeks = 12, sessionsPerWeek = 3, active = true
    )

    // findAll

    @Test
    fun `findAll returns all plans`() {
        every { trainingPlanDao.findAll(null) } returns listOf(buildPlan(1L), buildPlan(2L))
        assertEquals(2, service.findAll(null).size)
    }

    @Test
    fun `findAll filters by patientId`() {
        every { trainingPlanDao.findAll(1L) } returns listOf(buildPlan())
        assertEquals(1, service.findAll(1L).size)
    }

    // findById

    @Test
    fun `findById returns training plan response`() {
        every { trainingPlanDao.findById(1L) } returns buildPlan()

        val result = service.findById(1L)

        assertEquals(1L, result.id)
        assertEquals("Strength Plan", result.title)
        assertEquals("Mario Rossi", result.patientFullName)
        assertEquals("Anna Verdi", result.specialistFullName)
    }

    @Test
    fun `findById throws NoSuchElementException when not found`() {
        every { trainingPlanDao.findById(99L) } returns null

        val ex = assertThrows<NoSuchElementException> { service.findById(99L) }
        assert(ex.message!!.contains("99"))
    }

    // create

    @Test
    fun `create saves and returns new training plan`() {
        val saved = buildPlan(id = 5L)
        every { patientDao.findById(1L) } returns buildPatient()
        every { specialistDao.findById(10L) } returns buildSpecialist()
        every { trainingPlanDao.save(any()) } returns saved

        val result = service.create(buildRequest())

        assertEquals(5L, result.id)
        assertEquals("Strength Plan", result.title)
    }

    @Test
    fun `create throws NoSuchElementException when client not found`() {
        every { patientDao.findById(1L) } returns null

        assertThrows<NoSuchElementException> { service.create(buildRequest()) }
        verify(exactly = 0) { trainingPlanDao.save(any()) }
    }

    @Test
    fun `create throws NoSuchElementException when trainer not found`() {
        every { patientDao.findById(1L) } returns buildPatient()
        every { specialistDao.findById(10L) } returns null

        assertThrows<NoSuchElementException> { service.create(buildRequest()) }
        verify(exactly = 0) { trainingPlanDao.save(any()) }
    }

    // update

    @Test
    fun `update saves updated training plan`() {
        val existing = buildPlan(1L)
        val updated = existing.copy(title = "Updated Plan")
        every { trainingPlanDao.findById(1L) } returns existing
        every { patientDao.findById(1L) } returns buildPatient()
        every { specialistDao.findById(10L) } returns buildSpecialist()
        every { trainingPlanDao.save(any()) } returns updated

        val result = service.update(1L, buildRequest())

        assertEquals("Updated Plan", result.title)
    }

    @Test
    fun `update throws NoSuchElementException when plan not found`() {
        every { trainingPlanDao.findById(99L) } returns null

        assertThrows<NoSuchElementException> { service.update(99L, buildRequest()) }
        verify(exactly = 0) { trainingPlanDao.save(any()) }
    }

    // delete

    @Test
    fun `delete calls deleteById when plan exists`() {
        every { trainingPlanDao.findById(1L) } returns buildPlan()
        every { trainingPlanDao.deleteById(1L) } just runs

        service.delete(1L)

        verify { trainingPlanDao.deleteById(1L) }
    }

    @Test
    fun `delete throws NoSuchElementException when plan not found`() {
        every { trainingPlanDao.findById(99L) } returns null

        assertThrows<NoSuchElementException> { service.delete(99L) }
        verify(exactly = 0) { trainingPlanDao.deleteById(any()) }
    }
}
