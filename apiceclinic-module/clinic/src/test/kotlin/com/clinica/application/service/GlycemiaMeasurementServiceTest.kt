package com.clinica.application.service

import com.clinic.model.GlycemiaMeasurementRequest
import com.clinica.application.domain.GlycemiaClassification
import com.clinica.application.domain.GlycemiaContext
import com.clinica.application.domain.GlycemiaMeasurement
import com.clinica.application.domain.Patient
import com.clinica.application.domain.Specialist
import com.clinica.doors.outbound.database.dao.GlycemiaMeasurementDao
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
import java.time.ZoneOffset

@ExtendWith(MockKExtension::class)
class GlycemiaMeasurementServiceTest {

    @MockK private lateinit var glycemiaMeasurementDao: GlycemiaMeasurementDao
    @MockK private lateinit var patientDao: PatientDao
    @MockK private lateinit var specialistDao: SpecialistDao

    @InjectMockKs
    private lateinit var service: GlycemiaMeasurementService

    private val fixedTime = LocalDateTime.of(2026, 4, 20, 8, 0)

    private fun buildPatient(id: Long = 3L) = Patient(
        id = id, firstName = "Giulia", lastName = "Ferretti",
        fiscalCode = "FRTGLI90A41H501Z", birthDate = LocalDate.of(1990, 1, 1),
        email = "giulia@example.com"
    )

    private fun buildSpecialist(id: Long = 1L) = Specialist(
        id = id, firstName = "Simona", lastName = "Ruberti",
        role = "NUTRITIONIST", email = "simona@example.com"
    )

    private fun buildMeasurement(
        id: Long = 1L,
        context: GlycemiaContext = GlycemiaContext.A_DIGIUNO,
        valueMgDl: Int = 92
    ) = GlycemiaMeasurement(
        id = id,
        patient = buildPatient(),
        specialist = buildSpecialist(),
        measuredAt = fixedTime,
        valueMgDl = valueMgDl,
        context = context,
        notes = null,
        createdAt = fixedTime,
        updatedAt = fixedTime
    )

    private fun buildRequest(
        context: GlycemiaMeasurementRequest.Context = GlycemiaMeasurementRequest.Context.A_DIGIUNO,
        valueMgDl: Int = 92
    ) = GlycemiaMeasurementRequest(
        patientId = 3L,
        specialistId = 1L,
        measuredAt = fixedTime.atOffset(ZoneOffset.UTC),
        valueMgDl = valueMgDl,
        context = context,
        notes = null
    )

    @Test
    fun `findAll returns all measurements when patientId is null`() {
        every { glycemiaMeasurementDao.findAll(null) } returns
            listOf(buildMeasurement(1L), buildMeasurement(2L))

        val result = service.findAll(null)

        assertEquals(2, result.size)
        verify { glycemiaMeasurementDao.findAll(null) }
    }

    @Test
    fun `findAll filters by patientId`() {
        every { glycemiaMeasurementDao.findAll(3L) } returns listOf(buildMeasurement())

        val result = service.findAll(3L)

        assertEquals(1, result.size)
        assertEquals(3L, result[0].patient.id)
    }

    @Test
    fun `findById returns measurement response when found`() {
        every { glycemiaMeasurementDao.findById(1L) } returns buildMeasurement()

        val result = service.findById(1L)

        assertEquals(1L, result.id)
        assertEquals(GlycemiaContext.A_DIGIUNO, result.context)
        assertEquals(GlycemiaClassification.NORMALE, result.classification)
        assertEquals("Giulia", result.patient.firstName)
    }

    @Test
    fun `findById throws NoSuchElementException when not found`() {
        every { glycemiaMeasurementDao.findById(99L) } returns null

        val ex = assertThrows<NoSuchElementException> { service.findById(99L) }
        assert(ex.message!!.contains("99"))
    }

    @Test
    fun `create saves measurement with correct context and classification`() {
        val saved = buildMeasurement(id = 5L, context = GlycemiaContext.A_DIGIUNO, valueMgDl = 92)
        every { patientDao.findById(3L) } returns buildPatient()
        every { specialistDao.findById(1L) } returns buildSpecialist()
        every { glycemiaMeasurementDao.save(any()) } returns saved

        val result = service.create(buildRequest())

        assertEquals(5L, result.id)
        assertEquals(GlycemiaContext.A_DIGIUNO, result.context)
        assertEquals(GlycemiaClassification.NORMALE, result.classification)
        verify { glycemiaMeasurementDao.save(any()) }
    }

    @Test
    fun `create throws NoSuchElementException when patient not found`() {
        every { patientDao.findById(3L) } returns null

        assertThrows<NoSuchElementException> { service.create(buildRequest()) }
        verify(exactly = 0) { glycemiaMeasurementDao.save(any()) }
    }

    @Test
    fun `create throws NoSuchElementException when specialist not found`() {
        every { patientDao.findById(3L) } returns buildPatient()
        every { specialistDao.findById(1L) } returns null

        assertThrows<NoSuchElementException> { service.create(buildRequest()) }
        verify(exactly = 0) { glycemiaMeasurementDao.save(any()) }
    }

    @Test
    fun `update saves measurement with updated values`() {
        val existing = buildMeasurement(1L)
        val updated = existing.copy(valueMgDl = 130)
        every { glycemiaMeasurementDao.findById(1L) } returns existing
        every { patientDao.findById(3L) } returns buildPatient()
        every { specialistDao.findById(1L) } returns buildSpecialist()
        every { glycemiaMeasurementDao.save(any()) } returns updated

        val result = service.update(1L, buildRequest(valueMgDl = 130))

        assertEquals(130, result.valueMgDl)
        verify { glycemiaMeasurementDao.save(any()) }
    }

    @Test
    fun `update throws NoSuchElementException when measurement not found`() {
        every { glycemiaMeasurementDao.findById(99L) } returns null

        assertThrows<NoSuchElementException> { service.update(99L, buildRequest()) }
        verify(exactly = 0) { glycemiaMeasurementDao.save(any()) }
    }

    @Test
    fun `delete calls deleteById when measurement exists`() {
        every { glycemiaMeasurementDao.findById(1L) } returns buildMeasurement()
        every { glycemiaMeasurementDao.deleteById(1L) } just runs

        service.delete(1L)

        verify { glycemiaMeasurementDao.deleteById(1L) }
    }

    @Test
    fun `delete throws NoSuchElementException when measurement not found`() {
        every { glycemiaMeasurementDao.findById(99L) } returns null

        assertThrows<NoSuchElementException> { service.delete(99L) }
        verify(exactly = 0) { glycemiaMeasurementDao.deleteById(any()) }
    }
}
