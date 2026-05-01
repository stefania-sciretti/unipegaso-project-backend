package com.clinica.config

import com.clinica.doors.outbound.database.entities.UserEntity
import com.clinica.doors.outbound.database.repositories.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Popola il DB con utenti di default al avvio dell'applicazione (solo profilo dev)
 */
@Configuration
@Profile("dev")
class DataInitializer {

    @Bean
    fun initializeUsers(
        userRepository: UserRepository,
        passwordEncoder: PasswordEncoder
    ): CommandLineRunner {
        return CommandLineRunner {
            // Controlla se gli utenti già esistono
            val adminExists = userRepository.existsByUsername("admin")
            val userExists = userRepository.existsByUsername("user")

            if (!adminExists) {
                val admin = UserEntity(
                    username = "admin",
                    password = passwordEncoder.encode("admin123"),
                    email = "admin@apiceclinic.it",
                    role = "ROLE_ADMIN",
                    enabled = true
                )
                userRepository.save(admin)
                println("✓ Utente admin creato con password: admin123")
            }

            if (!userExists) {
                val user = UserEntity(
                    username = "user",
                    password = passwordEncoder.encode("user123"),
                    email = "user@apiceclinic.it",
                    role = "ROLE_USER",
                    enabled = true
                )
                userRepository.save(user)
                println("✓ Utente user creato con password: user123")
            }

            // Stampa i dettagli per verifica
            println("\n=== Utenti disponibili nel sistema ===")
            userRepository.findAll().forEach { u ->
                println("Username: ${u.username}, Role: ${u.role}, Enabled: ${u.enabled}")
            }
        }
    }
}
