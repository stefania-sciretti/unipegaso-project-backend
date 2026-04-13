package com.clinica.service.api

import com.clinica.dto.ClientRequest
import com.clinica.dto.ClientResponse

/**
 * Contract for client management operations.
 *
 * Controllers depend on this interface (Dependency Inversion Principle),
 * allowing the concrete implementation to be swapped without modifying consumers.
 */
interface ClientServicePort {
    fun findAll(): List<ClientResponse>
    fun findById(id: Long): ClientResponse
    fun search(query: String): List<ClientResponse>
    fun create(request: ClientRequest): ClientResponse
    fun update(id: Long, request: ClientRequest): ClientResponse
    fun delete(id: Long)
}
