package com.clinica.repository

import com.clinica.domain.ClientEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientRepository : JpaRepository<ClientEntity, Long> {
    fun existsByEmail(email: String): Boolean
    fun findByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCase(
        lastName: String, firstName: String
    ): List<ClientEntity>
}
