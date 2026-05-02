package com.clinica.application.domain

import java.time.LocalDateTime

data class GlycemiaMeasurement(
    val id: Long = 0,
    val patient: Patient,
    val specialist: Specialist,
    val measuredAt: LocalDateTime,
    val valueMgDl: Int,
    val context: GlycemiaContext,
    val notes: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    val classification: GlycemiaClassification
        get() = computeClassification(context, valueMgDl)
}
