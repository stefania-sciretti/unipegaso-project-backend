package com.clinica.doors.inbound.routes.controller

import com.clinica.application.service.AuthService
import com.clinica.dto.LoginRequest
import com.clinica.dto.LoginResponse
import com.clinica.dto.RegisterRequest
import com.clinica.security.JwtTokenProvider
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoint per l'autenticazione JWT")
class AuthController {
    private val logger = LoggerFactory.getLogger(AuthController::class.java)

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @Autowired
    private lateinit var authService: AuthService

    @PostMapping("/register")
    @Operation(summary = "Registra nuovo utente", description = "Crea un nuovo account utente")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Utente registrato"),
        ApiResponse(responseCode = "400", description = "Dati non validi")
    ])
    fun register(@RequestBody registerRequest: RegisterRequest): ResponseEntity<Any> {
        return try {
            val user = authService.registerUser(registerRequest)
            ResponseEntity.status(HttpStatus.CREATED)
                .body(mapOf(
                    "message" to "Utente registrato con successo",
                    "success" to true,
                    "username" to user.username,
                    "email" to user.email
                ))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(mapOf("message" to e.message, "success" to false))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("message" to "Errore durante la registrazione", "success" to false))
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login utente", description = "Effettua il login e restituisce un JWT token")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Login effettuato"),
        ApiResponse(responseCode = "401", description = "Credenziali non valide")
    ])
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        logger.debug("Login attempt: username={}", loginRequest.username)

        return try {
            val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    loginRequest.username,
                    loginRequest.password
                )
            )

            logger.debug("Authentication success: username={}, authorities={}",
                authentication.name, authentication.authorities)

            val token = jwtTokenProvider.generateToken(authentication)

            // Legge il ruolo effettivo dal principal autenticato
            val role = authentication.authorities
                .firstOrNull()?.authority ?: "ROLE_USER"

            val loginResponse = LoginResponse(
                accessToken = token,
                tokenType = "Bearer",
                username = loginRequest.username,
                role = role
            )

            logger.debug("Login successful: username={}, role={}", loginRequest.username, role)
            ResponseEntity.ok(loginResponse)
        } catch (e: AuthenticationException) {
            logger.warn("Authentication failed: username={}, error={}", loginRequest.username, e.message)
            ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(mapOf("message" to "Credenziali non valide", "success" to false))
        } catch (e: Exception) {
            logger.error("Login error: username={}", loginRequest.username, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("message" to "Errore durante il login", "success" to false))
        }
    }

    @GetMapping("/validate")
    @Operation(summary = "Valida token JWT", description = "Verifica se il token JWT è valido")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Risultato validazione token")
    ])
    fun validateToken(@RequestHeader("Authorization") token: String): ResponseEntity<Map<String, Any>> {
        val jwt = if (token.startsWith("Bearer ")) {
            token.substring(7)
        } else {
            token
        }

        val isValid = jwtTokenProvider.validateToken(jwt)
        val username = if (isValid) jwtTokenProvider.getUsernameFromToken(jwt) else null

        return ResponseEntity.ok(
            mapOf(
                "valid" to isValid,
                "username" to (username ?: ""),
                "message" to if (isValid) "Token valido" else "Token non valido"
            )
        )
    }

    @GetMapping("/test")
    @Operation(summary = "Test endpoint", description = "Endpoint di test per verificare il login")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "API funzionante")
    ])
    fun test(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf(
            "status" to "ok",
            "message" to "API di autenticazione funzionante"
        ))
    }
}
