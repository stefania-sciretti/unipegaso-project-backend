package com.clinica.application.service

import com.clinica.doors.outbound.database.entities.UserEntity
import com.clinica.doors.outbound.database.repositories.UserRepository
import com.clinica.dto.LoginRequest
import com.clinica.dto.RegisterRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun registerUser(registerRequest: RegisterRequest): UserEntity {
        require(!userRepository.existsByUsername(registerRequest.username)) {
            "Username '${registerRequest.username}' già esistente"
        }

        registerRequest.email?.takeIf { it.isNotEmpty() }?.let { email ->
            require(!userRepository.existsByEmail(email)) {
                "Email '$email' già registrata"
            }
        }

        val user = UserEntity(
            username = registerRequest.username,
            password = passwordEncoder.encode(registerRequest.password),
            email = registerRequest.email,
            enabled = true
        )

        return userRepository.save(user)
    }

    fun validateCredentials(loginRequest: LoginRequest): Boolean {
        val user = userRepository.findByUsername(loginRequest.username)
        return user.isPresent && passwordEncoder.matches(loginRequest.password, user.get().password)
    }
}
