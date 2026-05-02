package com.clinica.doors.outbound.database.entities

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "report")
class ReportEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fitness_appointment_id", nullable = false, unique = true)
    var fitnessAppointmentEntity: FitnessAppointmentEntity,

    @Column(name = "issued_date", nullable = false)
    var issuedDate: LocalDate = LocalDate.now(),

    @Column(nullable = false, columnDefinition = "TEXT")
    var diagnosis: String,

    @Column(columnDefinition = "TEXT")
    var prescription: String? = null,

    @Column(name = "specialist_notes", columnDefinition = "TEXT")
    var specialistNotes: String? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)
