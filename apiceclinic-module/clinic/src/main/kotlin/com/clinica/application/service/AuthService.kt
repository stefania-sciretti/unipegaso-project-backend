package com.clinica.application.service

import com.clinica.doors.outbound.database.entities.PatientEntity
import com.clinica.doors.outbound.database.entities.UserEntity
import com.clinica.doors.outbound.database.repositories.PatientRepository
import com.clinica.doors.outbound.database.repositories.UserRepository
import com.clinic.model.LoginRequest
import com.clinic.model.RegisterRequest
import com.clinic.model.RegisterResponse
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val patientRepository: PatientRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun registerUser(registerRequest: RegisterRequest): RegisterResponse {
        require(!userRepository.existsByUsername(registerRequest.username)) {
            "Username '${registerRequest.username}' già esistente"
        }
        require(!userRepository.existsByEmail(registerRequest.email)) {
            "Email '${registerRequest.email}' già registrata"
        }
        require(!patientRepository.existsByFiscalCode(registerRequest.fiscalCode)) {
            "Codice fiscale '${registerRequest.fiscalCode}' già registrato"
        }
        require(!patientRepository.existsByEmail(registerRequest.email)) {
            "Email '${registerRequest.email}' già associata a un paziente"
        }

        val user = userRepository.save(
            UserEntity(
                username = registerRequest.username,
                password = passwordEncoder.encode(registerRequest.password),
                email = registerRequest.email,
                role = "ROLE_USER",
                enabled = true
            )
        )

        val patient = patientRepository.save(
            PatientEntity(
                id = 0L,
                firstName = registerRequest.firstName,
                lastName = registerRequest.lastName,
                fiscalCode = registerRequest.fiscalCode,
                birthDate = registerRequest.birthDate,
                email = registerRequest.email,
                phone = registerRequest.phone,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )

        return RegisterResponse(
            message = "Registrazione completata con successo",
            success = true,
            username = user.username,
            email = user.email ?: "",
            patientId = patient.id
        )
    }

    fun validateCredentials(loginRequest: LoginRequest): Boolean {
        val user = userRepository.findByUsername(loginRequest.username)
        return user.isPresent && passwordEncoder.matches(loginRequest.password, user.get().password)
    }
}
