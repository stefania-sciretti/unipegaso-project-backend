package com.clinica.doors.outbound.database.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "areas")
class AreaEntity(
    @Id
    var id: Long,

    @Column(nullable = false, length = 100)
    var name: String
)
