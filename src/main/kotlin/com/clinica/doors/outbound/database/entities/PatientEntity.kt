package com.clinica.fitnessappointment.doors.outbound.database.entities

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "patient")
class PatientEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "first_name", nullable = false, length = 100)
    var firstName: String,

    @Column(name = "last_name", nullable = false, length = 100)
    var lastName: String,

    @Column(name = "fiscal_code", nullable = false, unique = true, length = 16)
    var fiscalCode: String,

    @Column(name = "birth_date", nullable = false)
    var birthDate: LocalDate,

    @Column(nullable = false, length = 255)
    var email: String,

    @Column(length = 20)
    var phone: String? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "patientEntity", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val appointmentEntities: MutableList<AppointmentEntity> = mutableListOf()
) {
    @PreUpdate
    fun onUpdate() {
        updatedAt = LocalDateTime.now()
    }
}
