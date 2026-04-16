package com.clinica.doors.outbound.database.mappers

import com.clinica.application.domain.Appointment
import com.clinica.application.domain.AppointmentStatus
import com.clinica.application.domain.Report
import com.clinica.doors.outbound.database.entities.AppointmentEntity
import com.clinica.doors.outbound.database.entities.DoctorEntity
import com.clinica.doors.outbound.database.entities.PatientEntity
import com.clinica.doors.outbound.database.entities.ReportEntity

fun AppointmentEntity.toDomain(): Appointment =
    Appointment(
        id = this.id,
        patient = this.patientEntity.toDomain(),
        doctor = this.doctor.toDomain(),
        scheduledAt = this.scheduledAt,
        visitType = this.visitType,
        status = AppointmentStatus.valueOf(this.status),
        notes = this.notes,
        updatedAt = this.updatedAt,
        report = this.report?.toReportDomain()
    )

fun Appointment.toEntity(
    patientEntityProvider: () -> PatientEntity,
    doctorEntityProvider: () -> DoctorEntity,
    existingEntity: AppointmentEntity? = null
): AppointmentEntity {
    val entity = existingEntity ?: AppointmentEntity(
        id = this.id,
        patientEntity = patientEntityProvider(),
        doctor = doctorEntityProvider(),
        scheduledAt = this.scheduledAt,
        visitType = this.visitType,
        status = this.status.name,
        notes = this.notes,
        updatedAt = this.updatedAt
    )
    existingEntity?.let {
        it.patientEntity = patientEntityProvider()
        it.doctor = doctorEntityProvider()
        it.scheduledAt = this.scheduledAt
        it.visitType = this.visitType
        it.status = this.status.name
        it.notes = this.notes
        it.updatedAt = this.updatedAt
    }
    return entity
}

private fun ReportEntity.toReportDomain(): Report =
    Report(
        id = this.id,
        appointment = this.appointmentEntity.toDomain(),
        issuedDate = this.issuedDate,
        diagnosis = this.diagnosis,
        prescription = this.prescription,
        doctorNotes = this.doctorNotes,
        updatedAt = this.updatedAt
    )
