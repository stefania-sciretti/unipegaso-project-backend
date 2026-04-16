package com.clinica.doors.outbound.database.entities

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "doctor")
class DoctorEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "first_name", nullable = false, length = 100)
    var firstName: String,

    @Column(name = "last_name", nullable = false, length = 100)
    var lastName: String,

    @Column(nullable = false, length = 200)
    var specialization: String,

    @Column(nullable = false, unique = true, length = 255)
    var email: String,

    @Column(name = "license_number", nullable = false, unique = true, length = 50)
    var licenseNumber: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "doctor", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val appointments: MutableList<AppointmentEntity> = mutableListOf()
)
