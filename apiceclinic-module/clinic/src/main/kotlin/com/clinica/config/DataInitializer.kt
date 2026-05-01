package com.clinica.config

import com.clinica.application.domain.AppointmentStatus
import com.clinica.doors.outbound.database.entities.*
import com.clinica.doors.outbound.database.repositories.*
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Popola il DB con dati demo realistici al avvio dell'applicazione (solo profilo dev)
 */
@Configuration
@Profile("dev")
class DataInitializer {

    @Bean
    fun initializeData(
        userRepository: UserRepository,
        patientRepository: PatientRepository,
        specialistRepository: SpecialistRepository,
        fitnessAppointmentRepository: FitnessAppointmentRepository,
        dietPlanRepository: DietPlanRepository,
        passwordEncoder: PasswordEncoder
    ): CommandLineRunner = CommandLineRunner {

        // ── Utenti ──────────────────────────────────────────────────────────
        if (userRepository.count() == 0L) {
            userRepository.save(UserEntity(username = "admin", password = passwordEncoder.encode("admin123"),
                email = "admin@apiceclinic.it", role = "ROLE_ADMIN", enabled = true))
            userRepository.save(UserEntity(username = "user", password = passwordEncoder.encode("user123"),
                email = "user@apiceclinic.it", role = "ROLE_USER", enabled = true))
            println("✓ Utenti demo creati (admin/admin123, user/user123)")
        }

        // ── Specialisti ──────────────────────────────────────────────────────
        if (specialistRepository.count() == 0L) {
            specialistRepository.saveAll(listOf(
                SpecialistEntity(firstName = "Francesca", lastName = "Marino",
                    role = "NUTRITIONIST", email = "f.marino@apiceclinic.it",
                    bio = "Specialista in nutrizione clinica e sportiva con 12 anni di esperienza."),
                SpecialistEntity(firstName = "Luca", lastName = "Ferretti",
                    role = "PERSONAL_TRAINER", email = "l.ferretti@apiceclinic.it",
                    bio = "Personal trainer certificato NSCA. Specializzato in riabilitazione posturale."),
                SpecialistEntity(firstName = "Sara", lastName = "Conti",
                    role = "PHYSIOTHERAPIST", email = "s.conti@apiceclinic.it",
                    bio = "Fisioterapista con specializzazione in fisioterapia sportiva e ortopedica."),
                SpecialistEntity(firstName = "Marco", lastName = "Pellegrini",
                    role = "NUTRITIONIST", email = "m.pellegrini@apiceclinic.it",
                    bio = "Dietista e nutrizionista, esperto in nutrizione pediatrica e geriatrica.")
            ))
            println("✓ 4 specialisti creati")
        }

        // ── Pazienti ─────────────────────────────────────────────────────────
        if (patientRepository.count() == 0L) {
            patientRepository.saveAll(listOf(
                PatientEntity(id = 0L, firstName = "Alessandro", lastName = "Russo",
                    fiscalCode = "RSSALS80A01H501R", birthDate = LocalDate.of(1980, 1, 1),
                    email = "a.russo@gmail.com", phone = "+39 333 1001001",
                    createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now()),
                PatientEntity(id = 0L, firstName = "Giovanna", lastName = "Ferrari",
                    fiscalCode = "FRRGNN85M41F205K", birthDate = LocalDate.of(1985, 8, 1),
                    email = "g.ferrari@gmail.com", phone = "+39 347 2002002",
                    createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now()),
                PatientEntity(id = 0L, firstName = "Roberto", lastName = "Esposito",
                    fiscalCode = "SPTRRT75T01L219V", birthDate = LocalDate.of(1975, 12, 1),
                    email = "r.esposito@libero.it", phone = "+39 328 3003003",
                    createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now()),
                PatientEntity(id = 0L, firstName = "Chiara", lastName = "Bianchi",
                    fiscalCode = "BNCCHR92D41A662Z", birthDate = LocalDate.of(1992, 4, 1),
                    email = "c.bianchi@outlook.com", phone = "+39 366 4004004",
                    createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now()),
                PatientEntity(id = 0L, firstName = "Davide", lastName = "Romano",
                    fiscalCode = "RMNDVD88P01G273X", birthDate = LocalDate.of(1988, 9, 1),
                    email = "d.romano@yahoo.it", phone = null,
                    createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now())
            ))
            println("✓ 5 pazienti creati")
        }

        // ── Appuntamenti fitness ──────────────────────────────────────────────
        if (fitnessAppointmentRepository.count() == 0L) {
            val patients = patientRepository.findAll()
            val specialists = specialistRepository.findAll()

            if (patients.size >= 3 && specialists.size >= 2) {
                fitnessAppointmentRepository.saveAll(listOf(
                    FitnessAppointmentEntity(
                        patientEntity = patients[0], specialist = specialists[0],
                        scheduledAt = LocalDateTime.now().plusDays(2),
                        serviceType = "Consulenza nutrizionale iniziale",
                        status = AppointmentStatus.BOOKED,
                        notes = "Prima visita. Il paziente vuole perdere peso."
                    ),
                    FitnessAppointmentEntity(
                        patientEntity = patients[1], specialist = specialists[1],
                        scheduledAt = LocalDateTime.now().plusDays(3),
                        serviceType = "Sessione personal training",
                        status = AppointmentStatus.BOOKED,
                        notes = "Focus su rinforzo muscolare arti inferiori."
                    ),
                    FitnessAppointmentEntity(
                        patientEntity = patients[2], specialist = specialists[2],
                        scheduledAt = LocalDateTime.now().minusDays(5),
                        serviceType = "Fisioterapia post-operatoria",
                        status = AppointmentStatus.COMPLETED,
                        notes = "Recupero post intervento al ginocchio."
                    ),
                    FitnessAppointmentEntity(
                        patientEntity = patients[0], specialist = specialists[1],
                        scheduledAt = LocalDateTime.now().plusDays(7),
                        serviceType = "Valutazione posturale",
                        status = AppointmentStatus.BOOKED,
                        notes = null
                    ),
                    FitnessAppointmentEntity(
                        patientEntity = patients[3], specialist = specialists[0],
                        scheduledAt = LocalDateTime.now().minusDays(2),
                        serviceType = "Piano alimentare personalizzato",
                        status = AppointmentStatus.COMPLETED,
                        notes = "Dieta ipocalorica bilanciata. Follow-up tra 30 giorni."
                    )
                ))
                println("✓ 5 appuntamenti fitness creati")
            }
        }

        // ── Piani dieta ───────────────────────────────────────────────────────
        if (dietPlanRepository.count() == 0L) {
            val patients = patientRepository.findAll()
            val specialists = specialistRepository.findAll()

            if (patients.size >= 2 && specialists.isNotEmpty()) {
                dietPlanRepository.saveAll(listOf(
                    DietPlanEntity(id = 0L,
                        patientEntity = patients[0], specialist = specialists[0],
                        title = "Piano dimagrimento graduale",
                        description = "Riduzione calorica moderata del 15%. Apporto proteico aumentato per preservare la massa muscolare.",
                        calories = 1800, durationWeeks = 12, active = true,
                        createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now()
                    ),
                    DietPlanEntity(id = 0L,
                        patientEntity = patients[1], specialist = specialists[3],
                        title = "Alimentazione sportiva",
                        description = "Piano nutrizionale per atleta amatoriale. Alto contenuto di carboidrati complessi e proteine.",
                        calories = 2400, durationWeeks = 8, active = true,
                        createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now()
                    )
                ))
                println("✓ 2 piani dieta creati")
            }
        }

        println("\n=== Apice Clinic — Dati demo caricati ===")
        println("Pazienti:    ${patientRepository.count()}")
        println("Specialisti: ${specialistRepository.count()}")
        println("Appuntamenti fitness: ${fitnessAppointmentRepository.count()}")
        println("Piani dieta: ${dietPlanRepository.count()}")
        println("=========================================")
    }
}

