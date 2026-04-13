package com.clinica.domain
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
@Entity
@Table(name = "glycemia_measurement")
class GlycemiaMeasurement(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    var client: Client,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trainer_id", nullable = false)
    var trainer: Trainer,
    @Column(name = "measured_at", nullable = false)
    var measuredAt: LocalDateTime,
    /** Valore glicemia in mg/dL */
    @Column(name = "value_mg_dl", nullable = false, precision = 6, scale = 2)
    var valueMgDl: BigDecimal,
    /**
     * Contesto della misurazione:
     * FASTING | POST_MEAL_1H | POST_MEAL_2H | RANDOM
     */
    @Column(nullable = false, length = 50)
    var context: String = "FASTING",
    @Column(columnDefinition = "TEXT")
    var notes: String? = null,
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    @PreUpdate
    fun onUpdate() { updatedAt = LocalDateTime.now() }
}
