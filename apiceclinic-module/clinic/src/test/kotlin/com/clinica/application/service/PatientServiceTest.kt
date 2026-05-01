package com.clinica.application.service

import com.clinica.application.domain.Patient
import com.clinica.doors.outbound.database.dao.PatientDao
import com.clinica.dto.PatientRequest
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class PatientServiceTest {

    @MockK
    private lateinit var dao: PatientDao

    @InjectMockKs
    private lateinit var service: PatientService

    private fun buildPatient(id: Long = 1L, fiscalCode: String = "RSSMRA80A01H501U") = Patient(
        id = id,
        firstName = "Mario",
        lastName = "Rossi",
        fiscalCode = fiscalCode,
        birthDate = LocalDate.of(1980, 1, 1),
        email = "mario.rossi@example.com",
        phone = "3331234567",
        updatedAt = LocalDateTime.now()
    )

    private fun buildRequest(fiscalCode: String = "RSSMRA80A01H501U") = PatientRequest(
        firstName = "Mario",
        lastName = "Rossi",
        fiscalCode = fiscalCode,
        birthDate = LocalDate.of(1980, 1, 1),
        email = "mario.rossi@example.com",
        phone = "3331234567"
    )

    // getAllPatients

    @Test
    fun `getAllPatients returns list from dao`() {
        val patients = listOf(buildPatient(1L), buildPatient(2L, "VRDLGI90B02F205S"))
        every { dao.getAllPatients() } returns patients

        val result = service.getAllPatients()

        assertEquals(2, result.size)
        verify { dao.getAllPatients() }
    }

    @Test
    fun `getAllPatients returns empty list when no patients`() {
        every { dao.getAllPatients() } returns emptyList()
        assertEquals(0, service.getAllPatients().size)
    }

    // findById

    @Test
    fun `findById returns patient when found`() {
        val patient = buildPatient()
        every { dao.findById(1L) } returns patient
        assertEquals(patient, service.findById(1L))
    }

    @Test
    fun `findById throws NoSuchElementException when not found`() {
        every { dao.findById(99L) } returns null
        val ex = assertThrows<NoSuchElementException> { service.findById(99L) }
        assert(ex.message!!.contains("99"))
    }

    // search

    @Test
    fun `search returns patients for valid term`() {
        val patients = listOf(buildPatient())
        every { dao.search("Mar") } returns patients

        val result = service.search("  Mar  ")

        assertEquals(1, result.size)
        verify { dao.search("Mar") }
    }

    @Test
    fun `search throws IllegalArgumentException when term is too short`() {
        assertThrows<IllegalArgumentException> { service.search("Ma") }
        verify(exactly = 0) { dao.search(any()) }
    }

    @Test
    fun `search throws IllegalArgumentException for blank term after trim`() {
        assertThrows<IllegalArgumentException> { service.search("  ") }
    }

    // create

    @Test
    fun `create saves and returns new patient`() {
        val request = buildRequest()
        val saved = buildPatient()
        every { dao.existsByFiscalCode(request.fiscalCode) } returns false
        every { dao.save(any()) } returns saved

        val result = service.create(request)

        assertEquals(saved, result)
        verify { dao.save(any()) }
    }

    @Test
    fun `create throws IllegalStateException when fiscal code already exists`() {
        val request = buildRequest()
        every { dao.existsByFiscalCode(request.fiscalCode) } returns true

        val ex = assertThrows<IllegalStateException> { service.create(request) }
        assert(ex.message!!.contains(request.fiscalCode))
        verify(exactly = 0) { dao.save(any()) }
    }

    // update

    @Test
    fun `update saves and returns updated patient`() {
        val existing = buildPatient(1L, "RSSMRA80A01H501U")
        val request = buildRequest("RSSMRA80A01H501U")
        every { dao.findById(1L) } returns existing
        every { dao.save(any()) } returns existing.copy(firstName = "Mario")

        val result = service.update(1L, request)

        assertNotNull(result)
        verify { dao.save(any()) }
    }

    @Test
    fun `update throws NoSuchElementException when patient not found`() {
        every { dao.findById(99L) } returns null

        assertThrows<NoSuchElementException> { service.update(99L, buildRequest()) }
        verify(exactly = 0) { dao.save(any()) }
    }

    @Test
    fun `update throws IllegalArgumentException when new fiscal code belongs to another patient`() {
        val existing = buildPatient(1L, "RSSMRA80A01H501U")
        val request = buildRequest("VRDLGI90B02F205S")
        every { dao.findById(1L) } returns existing
        every { dao.existsByFiscalCode("VRDLGI90B02F205S") } returns true

        val ex = assertThrows<IllegalArgumentException> { service.update(1L, request) }
        assert(ex.message!!.contains("VRDLGI90B02F205S"))
    }

    @Test
    fun `update allows keeping the same fiscal code`() {
        val existing = buildPatient(1L, "RSSMRA80A01H501U")
        val request = buildRequest("RSSMRA80A01H501U")
        every { dao.findById(1L) } returns existing
        every { dao.save(any()) } returns existing

        assertNotNull(service.update(1L, request))
    }

    // delete

    @Test
    fun `delete calls deleteById when patient exists`() {
        every { dao.findById(1L) } returns buildPatient()
        every { dao.deleteById(1L) } just runs

        service.delete(1L)

        verify { dao.deleteById(1L) }
    }

    @Test
    fun `delete throws NoSuchElementException when patient not found`() {
        every { dao.findById(99L) } returns null

        assertThrows<NoSuchElementException> { service.delete(99L) }
        verify(exactly = 0) { dao.deleteById(any()) }
    }
}
