package com.clinica.config

import com.clinica.application.domain.AppointmentStatusEnum
import com.clinica.doors.outbound.database.entities.*
import com.clinica.doors.outbound.database.repositories.*
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder
import java.math.BigDecimal
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
        appointmentRepository: AppointmentRepository,
        fitnessAppointmentRepository: FitnessAppointmentRepository,
        dietPlanRepository: DietPlanRepository,
        reportRepository: ReportRepository,
        passwordEncoder: PasswordEncoder
    ): CommandLineRunner = CommandLineRunner {

        // ── Utenti ──────────────────────────────────────────────────────────
        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(UserEntity(username = "admin", password = passwordEncoder.encode("admin123"),
                email = "admin@apiceclinic.it", role = "ROLE_ADMIN", enabled = true))
            println("✓ Utente admin creato (admin/admin123)")
        }
        if (!userRepository.existsByUsername("user")) {
            userRepository.save(UserEntity(username = "user", password = passwordEncoder.encode("user123"),
                email = "user@apiceclinic.it", role = "ROLE_USER", enabled = true))
            println("✓ Utente user creato (user/user123)")
        }

        // ── Specialisti ──────────────────────────────────────────────────────
        if (specialistRepository.count() == 0L) {
            specialistRepository.saveAll(listOf(
                SpecialistEntity(firstName = "Simona", lastName = "Ruberti",
                    role = "NUTRITIONIST", email = "s.ruberti@apiceclinic.it",
                    bio = "Nutrizionista sportiva con esperienza in piani alimentari per atleti e sportivi amatoriali."),
                SpecialistEntity(firstName = "Luca", lastName = "Siretta",
                    role = "PERSONAL_TRAINER", email = "l.siretta@apiceclinic.it",
                    bio = "Personal trainer certificato NSCA. Specializzato in allenamento funzionale e riabilitazione posturale."),
                SpecialistEntity(firstName = "Sandro", lastName = "Scironi",
                    role = "SPORT_DOCTOR", email = "s.scironi@apiceclinic.it",
                    bio = "Medico dello Sport con specializzazione in medicina preventiva e idoneità sportiva."),
                SpecialistEntity(firstName = "Cristiana", lastName = "Maratti",
                    role = "DIETOLOGIST", email = "c.maratti@apiceclinic.it",
                    bio = "Dietologa con esperienza in nutrizione clinica, disturbi metabolici e piani dietetici personalizzati."),
                SpecialistEntity(firstName = "Michele", lastName = "Lavori",
                    role = "PHYSIOTHERAPIST", email = "m.lavori@apiceclinic.it",
                    bio = "Fisioterapista specializzato in fisioterapia sportiva, ortopedica e riabilitazione post-operatoria.")
            ))
            println("✓ 5 specialisti creati")
        }

        // ── Pazienti ─────────────────────────────────────────────────────────
        if (patientRepository.count() == 0L) {
            data class P(val fn: String, val ln: String, val cf: String,
                         val y: Int, val m: Int, val d: Int,
                         val email: String, val phone: String? = null)

            val seeds = listOf(
                P("Alessandro","Russo","RSSALS80A01H501R",1980,1,1,"a.russo@gmail.com","+39 333 1001001"),
                P("Roberto","Esposito","SPTRRT75T01L219V",1975,12,1,"r.esposito@libero.it","+39 328 3003003"),
                P("Davide","Romano","RMNDVD88P01G273X",1988,9,1,"d.romano@yahoo.it"),
                P("Marco","Colombo","CLMMRC83C15H501A",1983,3,15,"m.colombo@gmail.com","+39 333 5005005"),
                P("Luca","Ricci","RCCLCU90L22F205B",1990,7,22,"l.ricci@libero.it"),
                P("Giovanni","Marino","MRNGNN72S08L219C",1972,11,8,"g.marino@gmail.com","+39 347 7007007"),
                P("Antonio","Greco","GRCNTN65E19H501D",1965,5,19,"a.greco@yahoo.it"),
                P("Matteo","Bruno","BRNMTT95B28F205E",1995,2,28,"m.bruno@gmail.com","+39 333 9009009"),
                P("Lorenzo","Gallo","GLLLRN87M14L219F",1987,8,14,"l.gallo@outlook.com"),
                P("Stefano","Conti","CNTSFN78D03H501G",1978,4,3,"s.conti@libero.it","+39 347 1101011"),
                P("Gabriele","De Luca","DLCGBR93R17F205H",1993,10,17,"g.deluca@gmail.com"),
                P("Andrea","Costa","CSTNDR69H25L219I",1969,6,25,"a.costa@gmail.com","+39 328 1301013"),
                P("Paolo","Giordano","GRDPLA82A30H501J",1982,1,30,"p.giordano@libero.it"),
                P("Fabio","Mancini","MNCFBA91C11F205K",1991,3,11,"f.mancini@yahoo.it","+39 366 1501015"),
                P("Simone","Rizzo","RZZSMN86P07L219L",1986,9,7,"s.rizzo@gmail.com"),
                P("Diego","Lombardi","LMBDGO77T20H501M",1977,12,20,"d.lombardi@hotmail.it","+39 333 1701017"),
                P("Riccardo","Moretti","MRTRCC94E05F205N",1994,5,5,"r.moretti@gmail.com"),
                P("Enrico","Barbieri","BRBNRC70M23L219O",1970,8,23,"e.barbieri@libero.it","+39 347 1901019"),
                P("Pietro","Fontana","FNTPTR85B16H501P",1985,2,16,"p.fontana@gmail.com"),
                P("Nicola","Santoro","SNTNCL98S01F205Q",1998,11,1,"n.santoro@gmail.com","+39 328 2101021"),
                P("Dario","Marini","MRNDRA63L14L219R",1963,7,14,"d.marini@yahoo.it"),
                P("Claudio","Rinaldi","RNLCLD79C27H501S",1979,3,27,"c.rinaldi@libero.it","+39 366 2301023"),
                P("Gianluca","Caruso","CRSGLN89R09F205T",1989,10,9,"g.caruso@gmail.com"),
                P("Massimo","Ferrara","FRRMSS74H18L219U",1974,6,18,"m.ferrara@outlook.com","+39 333 2501025"),
                P("Emanuele","Gatti","GTTMNL96A21H501V",1996,1,21,"e.gatti@gmail.com"),
                P("Salvatore","Battaglia","BTGSLV67P02F205W",1967,9,2,"s.battaglia@libero.it","+39 347 2701027"),
                P("Giuseppe","Leone","LNEGSP81D14L219X",1981,4,14,"g.leone@gmail.com"),
                P("Filippo","Longo","LNGFLP92L31H501Y",1992,7,31,"f.longo@yahoo.it","+39 328 2901029"),
                P("Francesco","Gentile","GNTFRN76T08F205Z",1976,12,8,"f.gentile@gmail.com"),
                P("Valerio","Martinelli","MRTVLR84E17L219A",1984,5,17,"v.martinelli@libero.it","+39 366 3101031"),
                P("Alberto","Vitale","VTLLBR71B22H501B",1971,2,22,"a.vitale@gmail.com"),
                P("Bruno","Messina","MSSBRN90M04F205C",1990,8,4,"b.messina@gmail.com","+39 333 3301033"),
                P("Carlo","Monti","MNTCRL68R29L219D",1968,10,29,"c.monti@libero.it"),
                P("Daniele","Sala","SLADNL97C13H501E",1997,3,13,"d.sala@gmail.com","+39 347 3501035"),
                P("Edoardo","De Angelis","DNGLRD86L06F205F",1986,7,6,"e.deangelis@yahoo.it"),
                P("Giorgio","Palumbo","PLMGRG73S19L219G",1973,11,19,"g.palumbo@gmail.com","+39 328 3701037"),
                P("Ivan","Parisi","PRSVNI93D25H501H",1993,4,25,"i.parisi@libero.it"),
                P("Jacopo","Ferraro","FRRJCP80P08F205I",1980,9,8,"j.ferraro@gmail.com","+39 366 3901039"),
                P("Luigi","Fabbri","FBBLGI59A15L219J",1959,1,15,"l.fabbri@libero.it"),
                P("Mario","Valentini","VLNMRA87H27H501K",1987,6,27,"m.valentini@yahoo.it","+39 333 4101041"),
                P("Nino","Pellegrini","PLGNNI95T03F205L",1995,12,3,"n.pellegrini@gmail.com"),
                P("Ottavio","Bernardi","BRNTTV64M11L219M",1964,8,11,"o.bernardi@gmail.com","+39 347 4301043"),
                P("Piero","Cattaneo","CTTPRA78C24H501N",1978,3,24,"p.cattaneo@libero.it"),
                P("Quirino","Rossetti","RSSQRN88R16F205O",1988,10,16,"q.rossetti@gmail.com","+39 328 4501045"),
                P("Rocco","Villa","VLLRCC75E29L219P",1975,5,29,"r.villa@yahoo.it"),
                P("Sergio","Bianco","BNCSRG92B07H501Q",1992,2,7,"s.bianco@gmail.com","+39 366 4701047"),
                P("Tommaso","Serra","SRRTMS83L20F205R",1983,7,20,"t.serra@libero.it"),
                P("Umberto","Pellegrino","PLGMBR70T02L219S",1970,12,2,"u.pellegrino@gmail.com","+39 333 4901049"),
                P("Vincenzo","Neri","NRIVNC96E15H501Z",1996,5,15,"v.neri@gmail.com"),
                P("Walter","Silvestri","SLVWLT61P28F205Y",1961,9,28,"w.silvestri@libero.it","+39 347 5001050"),

                P("Giovanna","Ferrari","FRRGNN85M41F205K",1985,8,1,"g.ferrari@gmail.com","+39 347 2002002"),
                P("Chiara","Bianchi","BNCCHR92D41A662Z",1992,4,1,"c.bianchi@outlook.com","+39 366 4004004"),
                P("Maria","Russo","RSSMRA78H52F205T",1978,6,12,"m.russo@libero.it"),
                P("Sara","Colombo","CLMSRA91S65H501U",1991,11,25,"s.colombo@gmail.com","+39 347 5301053"),
                P("Elena","Ricci","RCCLNE87C48L219V",1987,3,8,"e.ricci@yahoo.it"),
                P("Laura","Marino","MRNLRA69L59F205W",1969,7,19,"l.marino@gmail.com","+39 328 5501055"),
                P("Claudia","Greco","GRCCLD94A70H501X",1994,1,30,"c.greco@libero.it"),
                P("Valentina","Bruno","BRNVNT82R54L219Y",1982,10,14,"v.bruno@gmail.com","+39 366 5701057"),
                P("Francesca","Gallo","GLLFNC76D43F205Z",1976,4,3,"f.gallo@hotmail.it"),
                P("Sofia","Conti","CNTSFA99P57H501A",1999,9,17,"s.conti2@gmail.com","+39 333 5901059"),
                P("Giulia","De Luca","DLCGLI89B68L219B",1989,2,28,"g.deluca2@gmail.com"),
                P("Anna","Costa","CSTNNA63T45F205C",1963,12,5,"a.costa2@libero.it","+39 347 6101061"),
                P("Roberta","Giordano","GRDRRT85H62H501D",1985,6,22,"r.giordano@gmail.com"),
                P("Cristina","Mancini","MNCCST96C49L219E",1996,3,9,"c.mancini2@yahoo.it","+39 328 6301063"),
                P("Paola","Rizzo","RZZPLA73M56F205F",1973,8,16,"p.rizzo@gmail.com"),
                P("Daniela","Lombardi","LMBDNL88A69H501G",1988,1,29,"d.lombardi2@libero.it","+39 366 6501065"),
                P("Monica","Moretti","MRTMNC79L47L219H",1979,7,7,"m.moretti@gmail.com"),
                P("Alessia","Barbieri","BRBLSS93D61F205I",1993,4,21,"a.barbieri@gmail.com","+39 333 6701067"),
                P("Federica","Fontana","FNTFDR67S52H501J",1967,11,12,"f.fontana2@libero.it"),
                P("Cinzia","Santoro","SNTCNZ84E44L219K",1984,5,4,"c.santoro2@gmail.com","+39 347 6901069"),
                P("Ilaria","Marini","MRNLRI97P67F205L",1997,9,27,"i.marini2@gmail.com"),
                P("Silvia","Rinaldi","RNLSLV71B58H501M",1971,2,18,"s.rinaldi2@yahoo.it","+39 328 7101071"),
                P("Teresa","Caruso","CRSTRS86L41L219N",1986,7,1,"t.caruso@gmail.com"),
                P("Veronica","Ferrara","FRRVNC92T54F205O",1992,12,14,"v.ferrara2@libero.it","+39 366 7301073"),
                P("Sabrina","Gatti","GTTSBR75D46H501P",1975,4,6,"s.gatti2@gmail.com"),
                P("Michela","Battaglia","BTGMCL80R63L219Q",1980,10,23,"m.battaglia@yahoo.it","+39 333 7501075"),
                P("Luisa","Leone","LNELSU68H55F205R",1968,6,15,"l.leone2@gmail.com"),
                P("Isabella","Longo","LNGSBL95C68H501S",1995,3,28,"i.longo@libero.it","+39 347 7701077"),
                P("Giorgia","Gentile","GNTGRG83M51L219T",1983,8,11,"g.gentile2@gmail.com"),
                P("Elisa","Martinelli","MRTLSE77A64F205U",1977,1,24,"e.martinelli@gmail.com","+39 328 7901079"),
                P("Caterina","Vitale","VTLCTR90L57H501V",1990,7,17,"c.vitale2@libero.it"),
                P("Beatrice","Messina","MSSBTR63S70L219W",1963,11,30,"b.messina2@gmail.com","+39 366 8101081"),
                P("Angela","Monti","MNTNGL87D53F205X",1987,4,13,"a.monti@yahoo.it"),
                P("Adriana","Sala","SLDDRN98P66H501Y",1998,9,26,"a.sala@gmail.com","+39 333 8301083"),
                P("Nadia","De Angelis","DNGNDA74B49L219Z",1974,2,9,"n.deangelis@libero.it"),
                P("Ornella","Palumbo","PLMRNL89L62F205A",1989,7,22,"o.palumbo@gmail.com","+39 347 8501085"),
                P("Patrizia","Parisi","PRSPTZ66T45H501B",1966,12,5,"p.parisi@libero.it"),
                P("Rita","Ferraro","FRRRTA91E58L219C",1991,5,18,"r.ferraro2@gmail.com","+39 328 8701087"),
                P("Serena","Fabbri","FBBSRN78R41F205D",1978,10,1,"s.fabbri2@yahoo.it"),
                P("Tiziana","Valentini","VLNTZN85C54H501E",1985,3,14,"t.valentini@gmail.com","+39 366 8901089"),
                P("Vanessa","Pellegrini","PLGVNS93M67L219F",1993,8,27,"v.pellegrini2@libero.it"),
                P("Wanda","Bernardi","BRNWND70A50F205G",1970,1,10,"w.bernardi@gmail.com","+39 333 9101091"),
                P("Xenia","Cattaneo","CTTXNE96H63H501H",1996,6,23,"x.cattaneo@gmail.com"),
                P("Ylenia","Rossetti","RSSLNY81S46L219I",1981,11,6,"y.rossetti@libero.it","+39 347 9301093"),
                P("Zelda","Villa","VLLZLD87D59F205J",1987,4,19,"z.villa@gmail.com"),
                P("Assunta","Bianco","BNCSNT76P42H501K",1976,9,2,"a.bianco2@gmail.com","+39 328 9501095"),
                P("Bruna","Serra","SRRBNN93B55L219L",1993,2,15,"b.serra@yahoo.it"),
                P("Carmen","Pellegrino","PLGCMN68L68F205M",1968,7,28,"c.pellegrino@gmail.com","+39 366 9701097"),
                P("Donatella","Neri","NRNDNT84T51H501N",1984,12,11,"d.neri@libero.it"),
                P("Emanuela","Silvestri","SLVMNL79E64L219O",1979,5,24,"e.silvestri@gmail.com","+39 333 9901099"),
                P("Francesca","Montanari","MTNFNC91M47F205P",1991,8,7,"f.montanari@libero.it"),
                P("Giulia","Napolitano","NPLGLI88C59H501Q",1988,3,19,"g.napolitano@gmail.com","+39 347 9901100")
            )
            patientRepository.saveAll(seeds.map { p ->
                PatientEntity(id = 0L, firstName = p.fn, lastName = p.ln, fiscalCode = p.cf,
                    birthDate = LocalDate.of(p.y, p.m, p.d), email = p.email, phone = p.phone,
                    createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now())
            })
            println("✓ ${seeds.size} pazienti creati")
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
                        status = AppointmentStatusEnum.BOOKED,
                        notes = "Prima visita. Il paziente vuole perdere peso."
                    ),
                    FitnessAppointmentEntity(
                        patientEntity = patients[1], specialist = specialists[1],
                        scheduledAt = LocalDateTime.now().plusDays(3),
                        serviceType = "Sessione personal training",
                        status = AppointmentStatusEnum.BOOKED,
                        notes = "Focus su rinforzo muscolare arti inferiori."
                    ),
                    FitnessAppointmentEntity(
                        patientEntity = patients[2], specialist = specialists[2],
                        scheduledAt = LocalDateTime.now().minusDays(5),
                        serviceType = "Fisioterapia post-operatoria",
                        status = AppointmentStatusEnum.COMPLETED,
                        notes = "Recupero post intervento al ginocchio."
                    ),
                    FitnessAppointmentEntity(
                        patientEntity = patients[0], specialist = specialists[1],
                        scheduledAt = LocalDateTime.now().plusDays(7),
                        serviceType = "Valutazione posturale",
                        status = AppointmentStatusEnum.BOOKED,
                        notes = null
                    ),
                    FitnessAppointmentEntity(
                        patientEntity = patients[3], specialist = specialists[0],
                        scheduledAt = LocalDateTime.now().minusDays(2),
                        serviceType = "Piano alimentare personalizzato",
                        status = AppointmentStatusEnum.COMPLETED,
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

        // ── Appuntamenti clinici ──────────────────────────────────────────────
        if (appointmentRepository.count() == 0L) {
            val patients = patientRepository.findAll()
            val specialists  = specialistRepository.findAll()

            if (patients.size >= 5 && specialists.size >= 3) {
                appointmentRepository.saveAll(listOf(
                    AppointmentEntity(patientEntity = patients[0], specialistEntity = specialists[0],
                        scheduledAt = LocalDateTime.now().minusDays(30),
                        visitType = "Visita di controllo",
                        status = AppointmentStatusEnum.COMPLETED.name,
                        notes = "Paziente in buone condizioni generali.",
                        price = BigDecimal("80.00")),
                    AppointmentEntity(patientEntity = patients[1], specialistEntity = specialists[1],
                        scheduledAt = LocalDateTime.now().minusDays(20),
                        visitType = "Elettrocardiogramma",
                        status = AppointmentStatusEnum.COMPLETED.name,
                        notes = "ECG nella norma.",
                        price = BigDecimal("120.00")),
                    AppointmentEntity(patientEntity = patients[2], specialistEntity = specialists[2],
                        scheduledAt = LocalDateTime.now().minusDays(15),
                        visitType = "Visita ortopedica",
                        status = AppointmentStatusEnum.COMPLETED.name,
                        notes = "Lieve artrosi al ginocchio destro.",
                        price = BigDecimal("100.00")),
                    AppointmentEntity(patientEntity = patients[3], specialistEntity = specialists[0],
                        scheduledAt = LocalDateTime.now().minusDays(7),
                        visitType = "Prima visita",
                        status = AppointmentStatusEnum.COMPLETED.name,
                        notes = null,
                        price = BigDecimal("60.00")),
                    AppointmentEntity(patientEntity = patients[4], specialistEntity = specialists[1],
                        scheduledAt = LocalDateTime.now().plusDays(5),
                        visitType = "Visita cardiologica",
                        status = AppointmentStatusEnum.BOOKED.name,
                        notes = null,
                        price = BigDecimal("150.00"))
                ))
                println("✓ 5 appuntamenti clinici creati")
            }
        }

        // ── Referti ──────────────────────────────────────────────────────────
        if (reportRepository.count() == 0L) {
            val completedFitness = fitnessAppointmentRepository.findAll()
                .filter { it.status == AppointmentStatusEnum.COMPLETED }

            if (completedFitness.isNotEmpty()) {
                reportRepository.saveAll(completedFitness.map { appt ->
                    ReportEntity(
                        id = 0L,
                        fitnessAppointmentEntity = appt,
                        issuedDate = appt.scheduledAt.toLocalDate(),
                        diagnosis = "Referto di controllo — visita del ${appt.scheduledAt.toLocalDate()}",
                        prescription = null,
                        specialistNotes = "Nessuna anomalia rilevante."
                    )
                })
                println("✓ ${completedFitness.size} referti creati")
            }
        }

        println("\n=== Apice Clinic — Dati demo caricati ===")
        println("Pazienti:    ${patientRepository.count()}")
        println("Medici:      ${specialistRepository.count()}")
        println("Specialisti: ${specialistRepository.count()}")
        println("Appuntamenti clinici: ${appointmentRepository.count()}")
        println("Appuntamenti fitness: ${fitnessAppointmentRepository.count()}")
        println("Piani dieta: ${dietPlanRepository.count()}")
        println("Referti:     ${reportRepository.count()}")
        println("=========================================")
    }
}

