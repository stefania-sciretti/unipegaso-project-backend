package com.clinica.doors.outbound.database.entities

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "services")
class ServiceEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false, length = 255)
    var service: String,

    @Column(nullable = false, precision = 10, scale = 2)
    var price: BigDecimal,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "specialist_id", nullable = false)
    var specialist: SpecialistEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id")
    var area: AreaEntity? = null,
)
