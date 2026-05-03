package com.clinica.application.service

import com.clinic.model.ServiceRequest
import com.clinica.application.domain.ClinicService
import com.clinica.doors.outbound.database.dao.ServiceDao
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal

@ExtendWith(MockKExtension::class)
class ClinicServiceServiceTest {

    @MockK
    private lateinit var serviceDao: ServiceDao

    @InjectMockKs
    private lateinit var service: ClinicServiceService

    private fun buildClinicService(id: Long = 1L, specialistId: Long = 10L) = ClinicService(
        id = id,
        service = "Nutritional consultation",
        price = BigDecimal("80.00"),
        specialistId = specialistId,
        areaId = 1L,
        areaName = "Alimentazione"
    )

    private fun buildRequest(specialistId: Long = 10L) = ServiceRequest(
        service = "Nutritional consultation",
        price = 80.0,
        specialistId = specialistId
    )

    @Test
    fun `findAll returns all services`() {
        every { serviceDao.findAll() } returns listOf(buildClinicService(1L), buildClinicService(2L))

        val result = service.findAll()

        assertEquals(2, result.size)
        assertEquals(1L, result[0].id)
        assertEquals(2L, result[1].id)
    }

    @Test
    fun `findAll returns empty list when no services`() {
        every { serviceDao.findAll() } returns emptyList()

        assertEquals(0, service.findAll().size)
    }

    @Test
    fun `findBySpecialistId returns filtered services`() {
        every { serviceDao.findBySpecialistId(10L) } returns listOf(buildClinicService(1L, 10L))

        val result = service.findBySpecialistId(10L)

        assertEquals(1, result.size)
        assertEquals(10L, result[0].specialistId)
    }

    @Test
    fun `findBySpecialistId returns empty list when no match`() {
        every { serviceDao.findBySpecialistId(99L) } returns emptyList()

        assertEquals(0, service.findBySpecialistId(99L).size)
    }

    @Test
    fun `findById returns service response`() {
        every { serviceDao.findById(1L) } returns buildClinicService(1L)

        val result = service.findById(1L)

        assertEquals(1L, result.id)
        assertEquals("Nutritional consultation", result.service)
        assertEquals(BigDecimal("80.00"), result.price)
        assertEquals(1L, result.areaId)
        assertEquals("Alimentazione", result.areaName)
    }

    @Test
    fun `findById throws NoSuchElementException when not found`() {
        every { serviceDao.findById(99L) } returns null

        val ex = assertThrows<NoSuchElementException> { service.findById(99L) }
        assert(ex.message!!.contains("99"))
    }

    @Test
    fun `create saves and returns service response`() {
        val request = buildRequest()
        val saved = buildClinicService(id = 5L)
        every { serviceDao.save(any()) } returns saved

        val result = service.create(request)

        assertEquals(5L, result.id)
        assertEquals("Nutritional consultation", result.service)
        assertEquals(BigDecimal("80.00"), result.price)
    }

    @Test
    fun `create propagates NoSuchElementException when specialist not found`() {
        val request = buildRequest(specialistId = 999L)
        every { serviceDao.save(any()) } throws NoSuchElementException("Specialist 999 not found")

        assertThrows<NoSuchElementException> { service.create(request) }
    }

    @Test
    fun `update returns updated service`() {
        val request = buildRequest()
        val existing = buildClinicService(1L)
        val updated = existing.copy(service = "Updated service")
        every { serviceDao.findById(1L) } returns existing
        every { serviceDao.save(any()) } returns updated

        val result = service.update(1L, request)

        assertEquals(1L, result.id)
    }

    @Test
    fun `update throws when service not found`() {
        val request = buildRequest()
        every { serviceDao.findById(99L) } returns null

        assertThrows<NoSuchElementException> { service.update(99L, request) }
    }

    @Test
    fun `delete removes service`() {
        every { serviceDao.findById(1L) } returns buildClinicService(1L)
        every { serviceDao.deleteById(1L) } returns Unit

        service.delete(1L)

        verify(exactly = 1) { serviceDao.deleteById(1L) }
    }

    @Test
    fun `delete throws when service not found`() {
        every { serviceDao.findById(99L) } returns null

        assertThrows<NoSuchElementException> { service.delete(99L) }
    }
}
