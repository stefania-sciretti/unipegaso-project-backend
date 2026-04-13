package com.clinica.dto

import java.time.LocalDateTime

/**
 * Standard error response body returned by the REST API.
 *
 * Moved here from [com.clinica.config.GlobalExceptionHandler] to respect the
 * Single Responsibility Principle: the DTO package owns all data transfer objects,
 * while the exception handler only orchestrates HTTP responses.
 */
data class ErrorResponse(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val error: String,
    val message: String,
    val fieldErrors: Map<String, String> = emptyMap()
)
