package com.clinica.doors.outbound.database.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "staff")
class StaffEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "first_name", nullable = false, length = 100)
    var firstName: String,

    @Column(name = "last_name", nullable = false, length = 100)
    var lastName: String,

    @Column(nullable = false, length = 50)
    var role: String,

    @Column(columnDefinition = "TEXT")
    var bio: String? = null,

    @Column(nullable = false, unique = true, length = 255)
    var email: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "staff", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val appointments: MutableList<FitnessAppointment> = mutableListOf()
) {
    @PreUpdate
    fun onUpdate() { updatedAt = LocalDateTime.now() }
}
