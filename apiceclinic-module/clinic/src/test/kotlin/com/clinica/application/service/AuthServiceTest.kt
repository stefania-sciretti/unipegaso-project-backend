package com.clinica.application.service

import com.clinica.doors.outbound.database.entities.PatientEntity
import com.clinica.doors.outbound.database.entities.UserEntity
import com.clinica.doors.outbound.database.repositories.PatientRepository
import com.clinica.doors.outbound.database.repositories.UserRepository
import com.clinica.dto.LoginRequest
import com.clinica.dto.RegisterRequest
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockKExtension::class)
class AuthServiceTest {

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var patientRepository: PatientRepository

    @MockK
    private lateinit var passwordEncoder: PasswordEncoder

    @InjectMockKs
    private lateinit var service: AuthService

    private fun buildRegisterRequest(
        username: String = "mario.rossi",
        email: String = "mario@example.com",
        fiscalCode: String = "RSSMRA80A01H501Z"
    ) = RegisterRequest(
        firstName = "Mario",
        lastName = "Rossi",
        fiscalCode = fiscalCode,
        birthDate = LocalDate.of(1980, 1, 1),
        email = email,
        phone = null,
        username = username,
        password = "secret123"
    )

    private fun buildUserEntity(username: String = "mario.rossi", email: String = "mario@example.com") =
        UserEntity(id = 1L, username = username, password = "encoded_password", email = email, enabled = true)

    private fun buildPatientEntity(id: Long = 1L) = PatientEntity(
        id = id,
        firstName = "Mario",
        lastName = "Rossi",
        fiscalCode = "RSSMRA80A01H501Z",
        birthDate = LocalDate.of(1980, 1, 1),
        email = "mario@example.com",
        phone = null,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )

    @Test
    fun `registerUser creates user and patient when data is unique`() {
        val request = buildRegisterRequest()
        every { userRepository.existsByUsername(request.username) } returns false
        every { userRepository.existsByEmail(request.email) } returns false
        every { patientRepository.existsByFiscalCode(request.fiscalCode) } returns false
        every { patientRepository.existsByEmail(request.email) } returns false
        every { passwordEncoder.encode("secret123") } returns "encoded_password"
        every { userRepository.save(any()) } returns buildUserEntity()
        every { patientRepository.save(any()) } returns buildPatientEntity()

        val result = service.registerUser(request)

        assertTrue(result.success)
        assertEquals(request.username, result.username)
        verify { userRepository.save(any()) }
        verify { patientRepository.save(any()) }
    }

    @Test
    fun `registerUser throws when username already exists`() {
        val request = buildRegisterRequest()
        every { userRepository.existsByUsername(request.username) } returns true

        val ex = assertThrows<IllegalArgumentException> { service.registerUser(request) }
        assertTrue(ex.message!!.contains("mario.rossi") || ex.message!!.contains("Username"))
        verify(exactly = 0) { userRepository.save(any()) }
        verify(exactly = 0) { patientRepository.save(any()) }
    }

    @Test
    fun `registerUser throws when email already registered in users`() {
        val request = buildRegisterRequest()
        every { userRepository.existsByUsername(request.username) } returns false
        every { userRepository.existsByEmail(request.email) } returns true

        val ex = assertThrows<IllegalArgumentException> { service.registerUser(request) }
        assertTrue(ex.message!!.contains("Email") || ex.message!!.contains("mario@example.com"))
        verify(exactly = 0) { userRepository.save(any()) }
    }

    @Test
    fun `registerUser throws when fiscal code already exists`() {
        val request = buildRegisterRequest()
        every { userRepository.existsByUsername(request.username) } returns false
        every { userRepository.existsByEmail(request.email) } returns false
        every { patientRepository.existsByFiscalCode(request.fiscalCode) } returns true

        val ex = assertThrows<IllegalArgumentException> { service.registerUser(request) }
        assertTrue(ex.message!!.contains("RSSMRA80A01H501Z") || ex.message!!.contains("fiscale"))
        verify(exactly = 0) { patientRepository.save(any()) }
    }

    @Test
    fun `registerUser throws when email already associated to a patient`() {
        val request = buildRegisterRequest()
        every { userRepository.existsByUsername(request.username) } returns false
        every { userRepository.existsByEmail(request.email) } returns false
        every { patientRepository.existsByFiscalCode(request.fiscalCode) } returns false
        every { patientRepository.existsByEmail(request.email) } returns true

        val ex = assertThrows<IllegalArgumentException> { service.registerUser(request) }
        assertTrue(ex.message!!.contains("Email") || ex.message!!.contains("paziente"))
        verify(exactly = 0) { patientRepository.save(any()) }
    }

    @Test
    fun `registerUser encodes the password before saving`() {
        val request = buildRegisterRequest()
        every { userRepository.existsByUsername(request.username) } returns false
        every { userRepository.existsByEmail(request.email) } returns false
        every { patientRepository.existsByFiscalCode(request.fiscalCode) } returns false
        every { patientRepository.existsByEmail(request.email) } returns false
        every { passwordEncoder.encode("secret123") } returns "hashed_value"
        every { userRepository.save(any()) } returns buildUserEntity()
        every { patientRepository.save(any()) } returns buildPatientEntity()

        service.registerUser(request)

        verify { passwordEncoder.encode("secret123") }
    }

    @Test
    fun `validateCredentials returns true for correct username and password`() {
        every { userRepository.findByUsername("mario.rossi") } returns Optional.of(buildUserEntity())
        every { passwordEncoder.matches("secret123", "encoded_password") } returns true

        assertTrue(service.validateCredentials(LoginRequest("mario.rossi", "secret123")))
    }

    @Test
    fun `validateCredentials returns false when user not found`() {
        every { userRepository.findByUsername("unknown") } returns Optional.empty()

        assertFalse(service.validateCredentials(LoginRequest("unknown", "anyPassword")))
    }

    @Test
    fun `validateCredentials returns false when password does not match`() {
        every { userRepository.findByUsername("mario.rossi") } returns Optional.of(buildUserEntity())
        every { passwordEncoder.matches("wrongPassword", "encoded_password") } returns false

        assertFalse(service.validateCredentials(LoginRequest("mario.rossi", "wrongPassword")))
    }
}
