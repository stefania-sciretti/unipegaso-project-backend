package com.clinica.application.service

import com.clinica.doors.outbound.database.entities.UserEntity
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
import java.util.*

@ExtendWith(MockKExtension::class)
class AuthServiceTest {

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var passwordEncoder: PasswordEncoder

    @InjectMockKs
    private lateinit var service: AuthService

    private fun buildUserEntity(username: String = "mario", email: String? = "mario@example.com") =
        UserEntity(id = 1L, username = username, password = "encoded_password", email = email, enabled = true)

    @Test
    fun `registerUser saves new user when username and email are unique`() {
        val request = RegisterRequest(username = "mario", password = "secret123", email = "mario@example.com")
        every { userRepository.existsByUsername("mario") } returns false
        every { userRepository.existsByEmail("mario@example.com") } returns false
        every { passwordEncoder.encode("secret123") } returns "encoded_password"
        every { userRepository.save(any()) } returns buildUserEntity()

        val result = service.registerUser(request)

        assertNotNull(result)
        verify { userRepository.save(any()) }
    }

    @Test
    fun `registerUser throws IllegalArgumentException when username already exists`() {
        val request = RegisterRequest(username = "mario", password = "secret123")
        every { userRepository.existsByUsername("mario") } returns true

        val ex = assertThrows<IllegalArgumentException> { service.registerUser(request) }
        assert(ex.message!!.contains("mario") || ex.message!!.contains("Username"))
        verify(exactly = 0) { userRepository.save(any()) }
    }

    @Test
    fun `registerUser throws IllegalArgumentException when email already registered`() {
        val request = RegisterRequest(username = "mario2", password = "secret123", email = "mario@example.com")
        every { userRepository.existsByUsername("mario2") } returns false
        every { userRepository.existsByEmail("mario@example.com") } returns true

        val ex = assertThrows<IllegalArgumentException> { service.registerUser(request) }
        assert(ex.message!!.contains("Email") || ex.message!!.contains("mario@example.com"))
        verify(exactly = 0) { userRepository.save(any()) }
    }

    @Test
    fun `registerUser skips email check when email is null`() {
        val request = RegisterRequest(username = "mario3", password = "secret123", email = null)
        every { userRepository.existsByUsername("mario3") } returns false
        every { passwordEncoder.encode("secret123") } returns "encoded_password"
        every { userRepository.save(any()) } returns buildUserEntity("mario3", null)

        val result = service.registerUser(request)

        assertNotNull(result)
        verify(exactly = 0) { userRepository.existsByEmail(any()) }
    }

    @Test
    fun `registerUser encodes the password before saving`() {
        val request = RegisterRequest(username = "mario", password = "plaintext", email = null)
        every { userRepository.existsByUsername("mario") } returns false
        every { passwordEncoder.encode("plaintext") } returns "hashed_value"
        every { userRepository.save(any()) } answers { firstArg() }

        service.registerUser(request)

        verify { passwordEncoder.encode("plaintext") }
    }

    @Test
    fun `validateCredentials returns true for correct username and password`() {
        every { userRepository.findByUsername("mario") } returns Optional.of(buildUserEntity())
        every { passwordEncoder.matches("secret123", "encoded_password") } returns true

        assertTrue(service.validateCredentials(LoginRequest("mario", "secret123")))
    }

    @Test
    fun `validateCredentials returns false when user not found`() {
        every { userRepository.findByUsername("unknown") } returns Optional.empty()

        assertFalse(service.validateCredentials(LoginRequest("unknown", "anyPassword")))
    }

    @Test
    fun `validateCredentials returns false when password does not match`() {
        every { userRepository.findByUsername("mario") } returns Optional.of(buildUserEntity())
        every { passwordEncoder.matches("wrongPassword", "encoded_password") } returns false

        assertFalse(service.validateCredentials(LoginRequest("mario", "wrongPassword")))
    }
}
