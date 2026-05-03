# Fix Critical Issues Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Fix the 5 critical issues identified in the full codebase audit: delete dead code, fix UserEntity schema mismatch, extract mapper violation in ReportService, write missing ReportServiceTest, and replace DashboardService full-table-scan with proper aggregated queries.

**Architecture:** All changes follow the established hexagonal architecture: domain objects at centre, DAOs in `doors.outbound.database.dao`, mappers in `application.mappers` (service-level) and `doors.outbound.database.mappers` (outbound). Services must not contain mapping logic.

**Tech Stack:** Kotlin, Spring Boot 3, JPA/Hibernate, H2 (tests), JPQL, MockK, JUnit 5, Flyway

---

## File Map

| File | Action | Reason |
|---|---|---|
| `controller/AppointmentController.kt` | **Delete** | Entire file is dead (commented-out) code |
| `entities/UserEntity.kt` | **Modify** | Add `createdAt`/`updatedAt` fields missing from entity |
| `application/mappers/ReportServiceMapper.kt` | **Modify** | Add `FitnessAppointment.toAppointment()` extension function |
| `application/service/ReportService.kt` | **Modify** | Replace inline `Appointment(...)` construction with `toAppointment()` call |
| `test/.../ReportServiceTest.kt` | **Create** | New: full Mockk/JUnit 5 unit test coverage for ReportService |
| `repositories/AppointmentRepository.kt` | **Modify** | Add 5 JPQL aggregation query methods |
| `dao/DashboardDao.kt` | **Create** | New: encapsulates all dashboard aggregation queries |
| `application/service/DashboardService.kt` | **Modify** | Replace `appointmentRepository.findAll()` with `DashboardDao` method calls |

---

## Task 1: Delete dead AppointmentController

**Files:**
- Delete: `apiceclinic-module/clinic/src/main/kotlin/com/clinica/doors/inbound/routes/controller/AppointmentController.kt`

- [ ] **Step 1: Verify file is entirely commented out**

```bash
cat apiceclinic-module/clinic/src/main/kotlin/com/clinica/doors/inbound/routes/controller/AppointmentController.kt
```
Expected: file contains only `package` declaration + commented-out code block, no active class.

- [ ] **Step 2: Delete the file**

```bash
rm apiceclinic-module/clinic/src/main/kotlin/com/clinica/doors/inbound/routes/controller/AppointmentController.kt
```

- [ ] **Step 3: Verify nothing references AppointmentController**

```bash
grep -r "AppointmentController" apiceclinic-module --include="*.kt"
```
Expected: no results.

- [ ] **Step 4: Build to confirm no breakage**

```bash
mvn compile -pl apiceclinic-module/clinic -am --settings maven-settings.xml -q
```
Expected: `BUILD SUCCESS`

- [ ] **Step 5: Commit**

```bash
git add -A
git commit -m "chore: remove dead AppointmentController (fully commented-out file)

Co-authored-by: Copilot <223556219+Copilot@users.noreply.github.com>"
```

---

## Task 2: Fix UserEntity — add missing created_at / updated_at fields

The `users` table has `created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP` and `updated_at TIMESTAMP` columns. `UserEntity` has neither, causing Hibernate schema validation mismatch and invisible audit data.

**Files:**
- Modify: `apiceclinic-module/clinic/src/main/kotlin/com/clinica/doors/outbound/database/entities/UserEntity.kt`

- [ ] **Step 1: Write the updated UserEntity**

Replace the entire file content with:

```kotlin
package com.clinica.doors.outbound.database.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true, length = 255)
    val username: String = "",

    @Column(nullable = false, length = 255)
    val password: String = "",

    @Column(nullable = true, length = 255)
    val email: String? = null,

    @Column(nullable = false, length = 50)
    val role: String = "ROLE_USER",

    @Column(nullable = false)
    val enabled: Boolean = true,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null
)
```

- [ ] **Step 2: Compile to verify no breakage**

```bash
mvn compile -pl apiceclinic-module/clinic -am --settings maven-settings.xml -q
```
Expected: `BUILD SUCCESS`

> **Note:** `createdAt` is `val` (never changes after insert). `updatedAt` is `var` and nullable (null until first update, matching the DB `DEFAULT NULL`). Both `username`, `password`, `role` get explicit `length` to match the `VARCHAR(255)` / `VARCHAR(50)` in `V99__create_users_table.sql`.

- [ ] **Step 3: Run tests**

```bash
mvn test -pl apiceclinic-module/clinic -am --settings maven-settings.xml -q 2>&1 | tail -5
```
Expected: `Tests run: NNN, Failures: 0, Errors: 0` + `BUILD SUCCESS`

- [ ] **Step 4: Commit**

```bash
git add apiceclinic-module/clinic/src/main/kotlin/com/clinica/doors/outbound/database/entities/UserEntity.kt
git commit -m "fix(entity): add createdAt/updatedAt to UserEntity, align @Column constraints with V99 migration

Co-authored-by: Copilot <223556219+Copilot@users.noreply.github.com>"
```

---

## Task 3: Fix MAPPER VIOLATION — extract FitnessAppointment.toAppointment() from ReportService

`ReportService.create()` manually constructs `Appointment(...)` by copying fields from a `FitnessAppointment`. This is domain-to-domain mapping and must live in `ReportServiceMapper.kt`.

**Files:**
- Modify: `apiceclinic-module/clinic/src/main/kotlin/com/clinica/application/mappers/ReportServiceMapper.kt`
- Modify: `apiceclinic-module/clinic/src/main/kotlin/com/clinica/application/service/ReportService.kt`

- [ ] **Step 1: Add toAppointment() to ReportServiceMapper**

The current `ReportServiceMapper.kt` content:
```kotlin
package com.clinica.application.mappers

import com.clinic.model.ReportResponse
import com.clinica.application.domain.Report
import java.time.ZoneOffset

fun Report.toResponse(): ReportResponse = ...
```

Add the new extension function **after** the existing `toResponse()`:

```kotlin
package com.clinica.application.mappers

import com.clinic.model.ReportResponse
import com.clinica.application.domain.Appointment
import com.clinica.application.domain.FitnessAppointment
import com.clinica.application.domain.Report
import java.time.ZoneOffset

fun Report.toResponse(): ReportResponse =
    ReportResponse(
        id = id,
        appointmentId = appointment.id,
        patientFullName = appointment.patient.fullName,
        specialistFullName = appointment.specialist.fullName,
        visitType = appointment.visitType,
        scheduledAt = appointment.scheduledAt.atOffset(ZoneOffset.UTC),
        issuedDate = issuedDate,
        diagnosis = diagnosis,
        prescription = prescription,
        specialistNotes = specialistNotes,
        createdAt = createdAt.atOffset(ZoneOffset.UTC)
    )

fun FitnessAppointment.toAppointment(): Appointment =
    Appointment(
        id = id,
        patient = patient,
        specialist = specialist,
        scheduledAt = scheduledAt,
        visitType = serviceType,
        status = status,
        notes = notes,
        updatedAt = updatedAt
    )
```

- [ ] **Step 2: Update ReportService.create() to use the mapper**

Replace the inline `Appointment(...)` construction in `ReportService.kt` with `fitnessAppointment.toAppointment()`:

```kotlin
package com.clinica.application.service

import com.clinica.application.domain.AppointmentStatusEnum
import com.clinica.application.domain.Report
import com.clinica.application.mappers.toAppointment
import com.clinica.doors.outbound.database.dao.FitnessAppointmentDao
import com.clinica.doors.outbound.database.dao.ReportDao
import com.clinic.model.ReportRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class ReportService(
    private val reportDao: ReportDao,
    private val fitnessAppointmentDao: FitnessAppointmentDao
) {

    @Transactional(readOnly = true)
    fun findAll(): List<Report> =
        reportDao.findAll()

    @Transactional(readOnly = true)
    fun findById(id: Long): Report =
        reportDao.findById(id) ?: throw NoSuchElementException("Report $id not found")

    @Transactional(readOnly = true)
    fun findByAppointmentId(appointmentId: Long): Report =
        reportDao.findByAppointmentId(appointmentId)
            ?: throw NoSuchElementException("Report for appointment $appointmentId not found")

    @Transactional
    fun create(request: ReportRequest): Report {
        val fitnessAppointment = fitnessAppointmentDao.findById(request.appointmentId)
            ?: throw NoSuchElementException("Appointment ${request.appointmentId} not found")

        check(fitnessAppointment.status == AppointmentStatusEnum.COMPLETED) {
            "Report can only be created for COMPLETED appointments"
        }

        check(reportDao.findByAppointmentId(request.appointmentId) == null) {
            "Report already exists for appointment ${request.appointmentId}"
        }

        val now = LocalDateTime.now()
        val report = Report(
            id = 0L,
            appointment = fitnessAppointment.toAppointment(),
            issuedDate = LocalDate.now(),
            diagnosis = request.diagnosis,
            prescription = request.prescription,
            specialistNotes = request.specialistNotes,
            createdAt = now,
            updatedAt = now
        )
        return reportDao.save(report)
    }

    @Transactional
    fun update(id: Long, request: ReportRequest): Report {
        val existing = reportDao.findById(id) ?: throw NoSuchElementException("Report $id not found")
        val updated = existing.copy(
            diagnosis = request.diagnosis,
            prescription = request.prescription,
            specialistNotes = request.specialistNotes,
            updatedAt = LocalDateTime.now()
        )
        return reportDao.save(updated)
    }
}
```

> Note: `orThrow` replaced with explicit `?: throw NoSuchElementException(...)` to remove any implicit extension dependency.

- [ ] **Step 3: Compile**

```bash
mvn compile -pl apiceclinic-module/clinic -am --settings maven-settings.xml -q
```
Expected: `BUILD SUCCESS`

- [ ] **Step 4: Run tests**

```bash
mvn test -pl apiceclinic-module/clinic -am --settings maven-settings.xml -q 2>&1 | tail -5
```
Expected: `BUILD SUCCESS`, no failures.

- [ ] **Step 5: Commit**

```bash
git add apiceclinic-module/clinic/src/main/kotlin/com/clinica/application/mappers/ReportServiceMapper.kt \
        apiceclinic-module/clinic/src/main/kotlin/com/clinica/application/service/ReportService.kt
git commit -m "refactor(mapper): extract FitnessAppointment.toAppointment() from ReportService to ReportServiceMapper

Co-authored-by: Copilot <223556219+Copilot@users.noreply.github.com>"
```

---

## Task 4: Create ReportServiceTest

`ReportService` has 5 methods including guards for COMPLETED status and duplicate reports. Currently zero test coverage.

**Files:**
- Create: `apiceclinic-module/clinic/src/test/kotlin/com/clinica/application/service/ReportServiceTest.kt`

You need to know the exact domain structure:
- `Report` fields: `id: Long`, `appointment: Appointment`, `issuedDate: LocalDate`, `diagnosis: String`, `prescription: String?`, `specialistNotes: String?`, `createdAt: LocalDateTime`, `updatedAt: LocalDateTime`
- `Appointment` fields: `id: Long`, `patient: Patient`, `specialist: Specialist`, `scheduledAt: LocalDateTime`, `visitType: String`, `status: AppointmentStatusEnum`, `notes: String?`, `updatedAt: LocalDateTime`
- `FitnessAppointment` fields: `id: Long`, `patient: Patient`, `specialist: Specialist`, `scheduledAt: LocalDateTime`, `serviceType: String`, `status: AppointmentStatusEnum`, `notes: String?`, `updatedAt: LocalDateTime`
- `Patient` fields: `id: Long`, `firstName: String`, `lastName: String`, `fiscalCode: String`, `birthDate: LocalDate`, `email: String`
- `Specialist` fields: `id: Long`, `firstName: String`, `lastName: String`, `role: String`, `email: String`
- `ReportRequest` is an OpenAPI-generated class from `com.clinic.model` with fields: `appointmentId: Long`, `diagnosis: String`, `prescription: String?`, `specialistNotes: String?`

- [ ] **Step 1: Create the test file**

```kotlin
package com.clinica.application.service

import com.clinic.model.ReportRequest
import com.clinica.application.domain.Appointment
import com.clinica.application.domain.AppointmentStatusEnum
import com.clinica.application.domain.FitnessAppointment
import com.clinica.application.domain.Patient
import com.clinica.application.domain.Report
import com.clinica.application.domain.Specialist
import com.clinica.doors.outbound.database.dao.FitnessAppointmentDao
import com.clinica.doors.outbound.database.dao.ReportDao
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class ReportServiceTest {

    @MockK private lateinit var reportDao: ReportDao
    @MockK private lateinit var fitnessAppointmentDao: FitnessAppointmentDao

    @InjectMockKs
    private lateinit var service: ReportService

    private val fixedTime = LocalDateTime.of(2025, 6, 15, 10, 0)

    private fun buildPatient(id: Long = 1L) = Patient(
        id = id, firstName = "Mario", lastName = "Rossi",
        fiscalCode = "RSSMRA80A01H501U", birthDate = LocalDate.of(1980, 1, 1),
        email = "mario@example.com"
    )

    private fun buildSpecialist(id: Long = 10L) = Specialist(
        id = id, firstName = "Anna", lastName = "Verdi",
        role = "TRAINER", email = "anna@example.com"
    )

    private fun buildAppointment(id: Long = 5L) = Appointment(
        id = id, patient = buildPatient(), specialist = buildSpecialist(),
        scheduledAt = fixedTime, visitType = "Personal Training",
        status = AppointmentStatusEnum.COMPLETED, notes = null, updatedAt = fixedTime
    )

    private fun buildFitnessAppointment(
        id: Long = 5L,
        status: AppointmentStatusEnum = AppointmentStatusEnum.COMPLETED
    ) = FitnessAppointment(
        id = id, patient = buildPatient(), specialist = buildSpecialist(),
        scheduledAt = fixedTime, serviceType = "Personal Training",
        status = status, notes = null, updatedAt = fixedTime
    )

    private fun buildReport(id: Long = 1L) = Report(
        id = id,
        appointment = buildAppointment(),
        issuedDate = LocalDate.of(2025, 6, 15),
        diagnosis = "Healthy",
        prescription = "Rest",
        specialistNotes = "Good progress",
        createdAt = fixedTime,
        updatedAt = fixedTime
    )

    private fun buildRequest(appointmentId: Long = 5L) = ReportRequest(
        appointmentId = appointmentId,
        diagnosis = "Healthy",
        prescription = "Rest",
        specialistNotes = "Good progress"
    )

    // ── findAll ────────────────────────────────────────────────────────────────

    @Test
    fun `findAll returns all reports`() {
        every { reportDao.findAll() } returns listOf(buildReport(1L), buildReport(2L))

        val result = service.findAll()

        assertEquals(2, result.size)
        verify { reportDao.findAll() }
    }

    @Test
    fun `findAll returns empty list when no reports exist`() {
        every { reportDao.findAll() } returns emptyList()

        assertEquals(0, service.findAll().size)
    }

    // ── findById ───────────────────────────────────────────────────────────────

    @Test
    fun `findById returns report when found`() {
        every { reportDao.findById(1L) } returns buildReport(1L)

        val result = service.findById(1L)

        assertEquals(1L, result.id)
        assertEquals("Healthy", result.diagnosis)
        assertEquals(5L, result.appointment.id)
    }

    @Test
    fun `findById throws NoSuchElementException when not found`() {
        every { reportDao.findById(99L) } returns null

        val ex = assertThrows<NoSuchElementException> { service.findById(99L) }
        assert(ex.message!!.contains("99"))
    }

    // ── findByAppointmentId ────────────────────────────────────────────────────

    @Test
    fun `findByAppointmentId returns report when found`() {
        every { reportDao.findByAppointmentId(5L) } returns buildReport()

        val result = service.findByAppointmentId(5L)

        assertEquals(5L, result.appointment.id)
    }

    @Test
    fun `findByAppointmentId throws NoSuchElementException when not found`() {
        every { reportDao.findByAppointmentId(99L) } returns null

        val ex = assertThrows<NoSuchElementException> { service.findByAppointmentId(99L) }
        assert(ex.message!!.contains("99"))
    }

    // ── create ─────────────────────────────────────────────────────────────────

    @Test
    fun `create saves and returns report when appointment is COMPLETED`() {
        val fitnessAppt = buildFitnessAppointment(status = AppointmentStatusEnum.COMPLETED)
        val saved = buildReport(id = 10L)
        every { fitnessAppointmentDao.findById(5L) } returns fitnessAppt
        every { reportDao.findByAppointmentId(5L) } returns null
        every { reportDao.save(any()) } returns saved

        val result = service.create(buildRequest())

        assertEquals(10L, result.id)
        assertEquals("Healthy", result.diagnosis)
        verify { reportDao.save(any()) }
    }

    @Test
    fun `create maps FitnessAppointment fields to Appointment correctly`() {
        val fitnessAppt = buildFitnessAppointment(id = 7L, status = AppointmentStatusEnum.COMPLETED)
        val saved = buildReport()
        every { fitnessAppointmentDao.findById(7L) } returns fitnessAppt
        every { reportDao.findByAppointmentId(7L) } returns null
        every { reportDao.save(any()) } returns saved

        service.create(buildRequest(appointmentId = 7L))

        // Verify that the saved Report has appointment fields mapped from FitnessAppointment
        io.mockk.slot<Report>().let { slot ->
            verify {
                reportDao.save(withArg { report ->
                    assertEquals(7L, report.appointment.id)
                    assertEquals("Personal Training", report.appointment.visitType)
                    assertEquals(1L, report.appointment.patient.id)
                    assertEquals(10L, report.appointment.specialist.id)
                })
            }
        }
    }

    @Test
    fun `create throws NoSuchElementException when appointment not found`() {
        every { fitnessAppointmentDao.findById(99L) } returns null

        assertThrows<NoSuchElementException> { service.create(buildRequest(appointmentId = 99L)) }
        verify(exactly = 0) { reportDao.save(any()) }
    }

    @Test
    fun `create throws IllegalStateException when appointment is not COMPLETED`() {
        val bookedAppt = buildFitnessAppointment(status = AppointmentStatusEnum.BOOKED)
        every { fitnessAppointmentDao.findById(5L) } returns bookedAppt

        val ex = assertThrows<IllegalStateException> { service.create(buildRequest()) }
        assert(ex.message!!.contains("COMPLETED"))
        verify(exactly = 0) { reportDao.save(any()) }
    }

    @Test
    fun `create throws IllegalStateException when report already exists for appointment`() {
        val fitnessAppt = buildFitnessAppointment(status = AppointmentStatusEnum.COMPLETED)
        every { fitnessAppointmentDao.findById(5L) } returns fitnessAppt
        every { reportDao.findByAppointmentId(5L) } returns buildReport()

        val ex = assertThrows<IllegalStateException> { service.create(buildRequest()) }
        assert(ex.message!!.contains("already exists"))
        verify(exactly = 0) { reportDao.save(any()) }
    }

    // ── update ─────────────────────────────────────────────────────────────────

    @Test
    fun `update saves report with updated fields`() {
        val existing = buildReport(1L)
        val updated = existing.copy(diagnosis = "Updated diagnosis")
        every { reportDao.findById(1L) } returns existing
        every { reportDao.save(any()) } returns updated

        val result = service.update(1L, buildRequest())

        assertEquals("Updated diagnosis", result.diagnosis)
        verify { reportDao.save(any()) }
    }

    @Test
    fun `update preserves appointment and issuedDate from existing report`() {
        val existing = buildReport(1L)
        every { reportDao.findById(1L) } returns existing
        every { reportDao.save(any()) } returns existing

        service.update(1L, buildRequest())

        verify {
            reportDao.save(withArg { report ->
                assertEquals(existing.appointment.id, report.appointment.id)
                assertEquals(existing.issuedDate, report.issuedDate)
            })
        }
    }

    @Test
    fun `update throws NoSuchElementException when report not found`() {
        every { reportDao.findById(99L) } returns null

        val ex = assertThrows<NoSuchElementException> { service.update(99L, buildRequest()) }
        assert(ex.message!!.contains("99"))
        verify(exactly = 0) { reportDao.save(any()) }
    }
}
```

- [ ] **Step 2: Run the new test file**

```bash
mvn test -pl apiceclinic-module/clinic -am --settings maven-settings.xml \
  -Dtest=ReportServiceTest -q 2>&1 | tail -10
```
Expected: `Tests run: 13, Failures: 0, Errors: 0`

- [ ] **Step 3: Commit**

```bash
git add apiceclinic-module/clinic/src/test/kotlin/com/clinica/application/service/ReportServiceTest.kt
git commit -m "test(service): add ReportServiceTest covering all 5 methods and guard conditions

Co-authored-by: Copilot <223556219+Copilot@users.noreply.github.com>"
```

---

## Task 5: Fix DashboardService — replace full table scan with aggregated queries

**Root cause:** `DashboardService` calls `appointmentRepository.findAll()` which loads the entire `appointment` table into memory. All KPI calculations and chart groupings are done in Kotlin via `.filter`, `.groupBy`, `.sumOf`.

**Fix strategy:**
1. Add 5 JPQL aggregation query methods to `AppointmentRepository` (pure DB aggregation for KPIs — no data loaded to memory)
2. Create `DashboardDao` to encapsulate all aggregation calls and return the dashboard DTOs
3. Rewrite `DashboardService` to use `DashboardDao` (eliminates direct repository injection in service)

For charts (revenue-by-month, appointments-by-month), load only the last 12 months instead of all-time, then group in the DAO layer. This is acceptable because 12 months of a clinic's data is bounded, and avoids database-specific date-format functions (H2 vs PostgreSQL compatibility).

**Files:**
- Modify: `apiceclinic-module/clinic/src/main/kotlin/com/clinica/doors/outbound/database/repositories/AppointmentRepository.kt`
- Create: `apiceclinic-module/clinic/src/main/kotlin/com/clinica/doors/outbound/database/dao/DashboardDao.kt`
- Modify: `apiceclinic-module/clinic/src/main/kotlin/com/clinica/application/service/DashboardService.kt`

- [ ] **Step 1: Add aggregation queries to AppointmentRepository**

Replace the entire `AppointmentRepository.kt` content:

```kotlin
package com.clinica.doors.outbound.database.repositories

import com.clinica.doors.outbound.database.entities.AppointmentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDateTime

@Repository
interface AppointmentRepository : JpaRepository<AppointmentEntity, Long> {

    fun countByStatus(status: String): Long

    @Query("""
        SELECT a
        FROM AppointmentEntity a
        WHERE (:patientId IS NULL OR a.patientEntity.id = :patientId)
          AND (:specialistId IS NULL OR a.specialistEntity.id = :specialistId)
          AND (:status IS NULL OR a.status = :status)
        ORDER BY a.patientEntity.lastName
    """)
    fun search(
        @Param("patientId") patientId: Long?,
        @Param("specialistId") specialistId: Long?,
        @Param("status") status: String?
    ): List<AppointmentEntity>

    @Query("""
        SELECT COALESCE(SUM(a.price), 0)
        FROM AppointmentEntity a
        WHERE a.status = :status
          AND a.scheduledAt >= :from
          AND a.scheduledAt < :to
    """)
    fun sumPriceByStatusBetween(
        @Param("status") status: String,
        @Param("from") from: LocalDateTime,
        @Param("to") to: LocalDateTime
    ): BigDecimal

    @Query("""
        SELECT COUNT(DISTINCT a.patientEntity.id)
        FROM AppointmentEntity a
        WHERE a.status IN :statuses
          AND a.scheduledAt >= :from
          AND a.scheduledAt < :to
    """)
    fun countDistinctPatientsByStatusIn(
        @Param("statuses") statuses: List<String>,
        @Param("from") from: LocalDateTime,
        @Param("to") to: LocalDateTime
    ): Long

    @Query("""
        SELECT COUNT(a)
        FROM AppointmentEntity a
        WHERE a.scheduledAt >= :from
          AND a.scheduledAt < :to
    """)
    fun countByScheduledAtBetween(
        @Param("from") from: LocalDateTime,
        @Param("to") to: LocalDateTime
    ): Long

    @Query("""
        SELECT COUNT(a)
        FROM AppointmentEntity a
        WHERE a.status = :status
          AND a.scheduledAt >= :from
          AND a.scheduledAt < :to
    """)
    fun countByStatusBetween(
        @Param("status") status: String,
        @Param("from") from: LocalDateTime,
        @Param("to") to: LocalDateTime
    ): Long

    @Query("""
        SELECT a
        FROM AppointmentEntity a
        WHERE a.scheduledAt >= :from
        ORDER BY a.scheduledAt
    """)
    fun findAllFromDate(@Param("from") from: LocalDateTime): List<AppointmentEntity>
}
```

- [ ] **Step 2: Create DashboardDao**

Create new file `apiceclinic-module/clinic/src/main/kotlin/com/clinica/doors/outbound/database/dao/DashboardDao.kt`:

```kotlin
package com.clinica.doors.outbound.database.dao

import com.clinica.dto.AppointmentsByMonth
import com.clinica.dto.DashboardStatsResponse
import com.clinica.dto.KpiStats
import com.clinica.dto.RevenueByMonth
import com.clinica.dto.RevenueByService
import com.clinica.doors.outbound.database.repositories.AppointmentRepository
import com.clinica.doors.outbound.database.repositories.PatientRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class DashboardDao(
    private val appointmentRepository: AppointmentRepository,
    private val patientRepository: PatientRepository
) {
    private val monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM")

    @Transactional(readOnly = true)
    fun getDashboardStats(): DashboardStatsResponse {
        val now = LocalDateTime.now()
        val startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0)
        val startOfNextMonth = startOfMonth.plusMonths(1)
        val startOfPrevMonth = startOfMonth.minusMonths(1)
        val startOf12MonthsAgo = startOfMonth.minusMonths(11)

        return DashboardStatsResponse(
            kpi = computeKpi(now, startOfMonth, startOfNextMonth, startOfPrevMonth),
            revenueByMonth = computeRevenueByMonth(startOf12MonthsAgo),
            appointmentsByMonth = computeAppointmentsByMonth(startOf12MonthsAgo),
            revenueByService = computeRevenueByService(startOf12MonthsAgo)
        )
    }

    private fun computeKpi(
        now: LocalDateTime,
        startOfMonth: LocalDateTime,
        startOfNextMonth: LocalDateTime,
        startOfPrevMonth: LocalDateTime
    ): KpiStats {
        val revenueMonth = appointmentRepository
            .sumPriceByStatusBetween("COMPLETED", startOfMonth, startOfNextMonth)
            .toDouble()
        val revenuePrevMonth = appointmentRepository
            .sumPriceByStatusBetween("COMPLETED", startOfPrevMonth, startOfMonth)
            .toDouble()
        val activePatients = appointmentRepository
            .countDistinctPatientsByStatusIn(listOf("BOOKED", "CONFIRMED"), startOfMonth, startOfNextMonth)
        val newPatients = patientRepository.countByCreatedAtBetween(startOfMonth, now)
        val appointmentsMonth = appointmentRepository
            .countByScheduledAtBetween(startOfMonth, startOfNextMonth)
        val cancelledMonth = appointmentRepository
            .countByStatusBetween("CANCELLED", startOfMonth, startOfNextMonth)
        val completedOrConfirmedMonth = appointmentRepository
            .countByStatusBetween("COMPLETED", startOfMonth, startOfNextMonth) +
            appointmentRepository.countByStatusBetween("CONFIRMED", startOfMonth, startOfNextMonth)

        val cancellationRate = if (appointmentsMonth > 0)
            cancelledMonth.toDouble() / appointmentsMonth * 100.0
        else 0.0
        val agendaOccupancy = if (appointmentsMonth > 0)
            completedOrConfirmedMonth.toDouble() / appointmentsMonth * 100.0
        else 0.0

        return KpiStats(
            revenueMonth = revenueMonth,
            revenuePrevMonth = revenuePrevMonth,
            activePatients = activePatients,
            newPatients = newPatients,
            appointmentsMonth = appointmentsMonth,
            cancellationRate = cancellationRate,
            agendaOccupancy = agendaOccupancy
        )
    }

    private fun computeRevenueByMonth(from: LocalDateTime): List<RevenueByMonth> =
        appointmentRepository.findAllFromDate(from)
            .filter { it.status == "COMPLETED" }
            .groupBy { it.scheduledAt.format(monthFormatter) }
            .map { (month, appts) -> RevenueByMonth(month, appts.sumOf { it.price }.toDouble()) }
            .sortedBy { it.month }

    private fun computeAppointmentsByMonth(from: LocalDateTime): List<AppointmentsByMonth> =
        appointmentRepository.findAllFromDate(from)
            .groupBy { it.scheduledAt.format(monthFormatter) }
            .map { (month, appts) ->
                AppointmentsByMonth(
                    month = month,
                    booked = appts.count { it.status == "BOOKED" }.toLong(),
                    completed = appts.count { it.status == "COMPLETED" }.toLong(),
                    cancelled = appts.count { it.status == "CANCELLED" }.toLong()
                )
            }
            .sortedBy { it.month }

    private fun computeRevenueByService(from: LocalDateTime): List<RevenueByService> =
        appointmentRepository.findAllFromDate(from)
            .filter { it.status == "COMPLETED" }
            .groupBy { it.visitType }
            .map { (service, appts) -> RevenueByService(service, appts.sumOf { it.price }.toDouble()) }
            .sortedByDescending { it.total }
}
```

- [ ] **Step 3: Rewrite DashboardService to use DashboardDao**

Replace entire `DashboardService.kt`:

```kotlin
package com.clinica.application.service

import com.clinica.doors.outbound.database.dao.DashboardDao
import com.clinica.dto.DashboardStatsResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DashboardService(
    private val dashboardDao: DashboardDao
) {
    @Transactional(readOnly = true)
    fun getDashboard(): DashboardStatsResponse =
        dashboardDao.getDashboardStats()
}
```

- [ ] **Step 4: Compile**

```bash
mvn compile -pl apiceclinic-module/clinic -am --settings maven-settings.xml -q
```
Expected: `BUILD SUCCESS`

- [ ] **Step 5: Run all tests**

```bash
mvn test -pl apiceclinic-module/clinic -am --settings maven-settings.xml -q 2>&1 | tail -5
```
Expected: `BUILD SUCCESS`, all tests pass.

> If `DashboardServiceTest` fails because it mocks `AppointmentRepository`/`PatientRepository` directly: update the test to mock `DashboardDao` instead and return a `DashboardStatsResponse` directly. The test for `DashboardService` after refactoring should simply be:
> ```kotlin
> every { dashboardDao.getDashboardStats() } returns buildStats()
> assertEquals(buildStats(), service.getDashboard())
> ```

- [ ] **Step 6: Commit**

```bash
git add \
  apiceclinic-module/clinic/src/main/kotlin/com/clinica/doors/outbound/database/repositories/AppointmentRepository.kt \
  apiceclinic-module/clinic/src/main/kotlin/com/clinica/doors/outbound/database/dao/DashboardDao.kt \
  apiceclinic-module/clinic/src/main/kotlin/com/clinica/application/service/DashboardService.kt
git commit -m "perf(dashboard): replace full-table-scan with aggregated JPQL queries via DashboardDao

- AppointmentRepository: add sumPriceByStatusBetween, countDistinct*, countByStatus/ScheduledAt queries
- DashboardDao: encapsulates all KPI and chart aggregation logic
- DashboardService: now delegates entirely to DashboardDao (no direct repository injection)

Co-authored-by: Copilot <223556219+Copilot@users.noreply.github.com>"
```

---

## Final Verification

- [ ] **Run full test suite**

```bash
mvn test -pl apiceclinic-module/clinic -am --settings maven-settings.xml 2>&1 | grep -E "Tests run:|BUILD"
```
Expected: all test counts, 0 failures, `BUILD SUCCESS`

- [ ] **Verify deleted file is gone**

```bash
ls apiceclinic-module/clinic/src/main/kotlin/com/clinica/doors/inbound/routes/controller/ | grep -i appointment
```
Expected: no output.
