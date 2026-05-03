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
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    val appointmentEntity: AppointmentEntity,

    @Column(name = "issued_date", nullable = false)
    var issuedDate: LocalDate = LocalDate.now(),

    @Column(nullable = false, columnDefinition = "TEXT")
    var diagnosis: String,

    @Column(columnDefinition = "TEXT")
    var prescription: String? = null,

    @Column(name = "specialist_notes", columnDefinition = "TEXT")
    var specialistNotes: String? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)
