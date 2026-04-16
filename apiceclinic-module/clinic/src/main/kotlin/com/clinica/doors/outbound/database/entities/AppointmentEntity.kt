package com.clinica.doors.outbound.database.entities

import java.time.LocalDateTime
import javax.persistence.*
import javax.persistence.CascadeType


@Entity
@Table(name = "appointment")
class AppointmentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    var patientEntity: PatientEntity,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    var doctor: DoctorEntity,

    @Column(name = "scheduled_at", nullable = false)
    var scheduledAt: LocalDateTime,

    @Column(name = "visit_type", nullable = false, length = 200)
    var visitType: String,

    @Column(nullable = false, length = 20)
    var status: String,

    @Column(columnDefinition = "TEXT")
    var notes: String? = null,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @OneToOne(mappedBy = "appointmentEntity", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var report: ReportEntity? = null,
)
