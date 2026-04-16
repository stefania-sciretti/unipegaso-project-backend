package com.clinica.doors.outbound.database.entities

import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "report")
class ReportEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    var appointmentEntity: AppointmentEntity,

    @Column(name = "issued_date", nullable = false)
    var issuedDate: LocalDate = LocalDate.now(),

    @Column(nullable = false, columnDefinition = "TEXT")
    var diagnosis: String,

    @Column(columnDefinition = "TEXT")
    var prescription: String? = null,

    @Column(name = "doctor_notes", columnDefinition = "TEXT")
    var doctorNotes: String? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)
