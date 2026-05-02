package com.clinica.application.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class AppointmentStatusEnumTest {

    @ParameterizedTest
    @ValueSource(strings = ["BOOKED", "CONFIRMED", "COMPLETED", "CANCELLED"])
    fun `parse returns correct status for valid uppercase values`(value: String) {
        val status = AppointmentStatusEnum.parse(value)
        assertEquals(value, status.name)
    }

    @ParameterizedTest
    @ValueSource(strings = ["booked", "confirmed", "completed", "cancelled"])
    fun `parse is case-insensitive`(value: String) {
        val status = AppointmentStatusEnum.parse(value)
        assertEquals(value.uppercase(), status.name)
    }

    @Test
    fun `parse throws IllegalArgumentException for unknown status`() {
        val ex = assertThrows<IllegalArgumentException> {
            AppointmentStatusEnum.parse("PENDING")
        }
        assert(ex.message!!.contains("PENDING"))
    }

    @Test
    fun `parse throws IllegalArgumentException for blank string`() {
        assertThrows<IllegalArgumentException> {
            AppointmentStatusEnum.parse("")
        }
    }
}
