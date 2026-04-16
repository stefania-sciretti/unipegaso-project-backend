package com.clinica.doors.outbound.database.entities

import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "patient")
class PatientEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "first_name", nullable = false, length = 100)
    var firstName: String,

    @Column(name = "last_name", nullable = false, length = 100)
    var lastName: String,

    @Column(name = "fiscal_code", nullable = false, unique = true, length = 16)
    var fiscalCode: String,

    @Column(name = "birth_date", nullable = false)
    var birthDate: LocalDate,

    @Column()
    var email: String,

    @Column()
    var phone: String? = null,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "patientEntity", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val appointmentEntities: MutableList<AppointmentEntity> = mutableListOf(),
)
