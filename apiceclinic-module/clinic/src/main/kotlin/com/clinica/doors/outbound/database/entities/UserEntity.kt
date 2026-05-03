package com.clinica.doors.outbound.database.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true, length = 255)
    val username: String = "",

    @Column(nullable = false, length = 255)
    val password: String = "",

    @Column(nullable = true, length = 255)
    val email: String? = null,

    @Column(nullable = false, length = 50)
    val role: String = "ROLE_USER",

    @Column(nullable = false)
    val enabled: Boolean = true,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null
)
