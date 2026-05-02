package com.clinica.application.domain

enum class AppointmentStatusEnum {
    BOOKED,
    CONFIRMED,
    COMPLETED,
    CANCELLED;

    companion object {
        fun parse(value: String): AppointmentStatusEnum =
            entries.find { it.name == value.uppercase() }
                ?: throw IllegalArgumentException("Invalid appointment status: '$value'")
    }
}
