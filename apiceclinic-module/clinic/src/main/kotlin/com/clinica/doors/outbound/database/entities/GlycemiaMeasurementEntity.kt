package com.clinica.doors.outbound.database.entities

import com.clinica.application.domain.GlycemiaContext
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "glycemia_measurement")
class GlycemiaMeasurementEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    var patientEntity: PatientEntity,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "specialist_id", nullable = false)
    var specialistEntity: SpecialistEntity,

    @Column(name = "measured_at", nullable = false)
    var measuredAt: LocalDateTime,

    @Column(name = "value_mg_dl", nullable = false)
    var valueMgDl: Int,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var context: GlycemiaContext,

    @Column(columnDefinition = "TEXT")
    var notes: String? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)
