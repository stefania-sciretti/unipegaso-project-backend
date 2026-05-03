package com.clinica.doors.outbound.database.entities

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime


@Entity
@Table(name = "appointment")
class AppointmentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    var patientEntity: PatientEntity,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "specialist_id", nullable = false)
    var specialistEntity: SpecialistEntity,

    @Column(name = "scheduled_at", nullable = false)
    var scheduledAt: LocalDateTime,

    @Column(name = "visit_type", nullable = false, length = 200)
    var visitType: String,

    @Column(nullable = false, length = 20)
    var status: String,

    @Column(columnDefinition = "TEXT")
    var notes: String? = null,

    @Column(nullable = false, precision = 10, scale = 2)
    var price: BigDecimal = BigDecimal.ZERO,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)
