package com.clinica.doors.outbound.database.entities

import com.clinica.application.domain.AppointmentStatus
import jakarta.persistence.*
import java.time.LocalDateTime

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
    var doctor: Doctor,

    @Column(name = "scheduled_at", nullable = false)
    var scheduledAt: LocalDateTime,

    @Column(name = "visit_type", nullable = false, length = 200)
    var visitType: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: AppointmentStatus = AppointmentStatus.BOOKED,

    @Column(columnDefinition = "TEXT")
    var notes: String? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @OneToOne(mappedBy = "appointmentEntity", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var report: Report? = null
) {
    @PreUpdate
    fun onUpdate() {
        updatedAt = LocalDateTime.now()
    }
}
