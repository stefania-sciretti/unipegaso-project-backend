-- V107 H2: Merge fitness_appointment table into appointment

-- Step 1: Add new columns one at a time (H2 syntax)
ALTER TABLE appointment ADD COLUMN service_type VARCHAR(200);
ALTER TABLE appointment ADD COLUMN created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE appointment ADD COLUMN area_id      BIGINT REFERENCES areas(id);
ALTER TABLE appointment ADD COLUMN _fitness_id  BIGINT;

-- Step 2: Backfill service_type from visit_type
UPDATE appointment SET service_type = visit_type;
ALTER TABLE appointment ALTER COLUMN service_type VARCHAR(200) NOT NULL;

-- Step 3: Insert fitness_appointment rows (no-op in test env — table is empty)
INSERT INTO appointment (patient_id, specialist_id, scheduled_at, service_type, status, notes, price, created_at, area_id, updated_at, _fitness_id)
SELECT
    fa.patient_id,
    fa.trainer_id,
    fa.scheduled_at,
    fa.service_type,
    fa.status,
    fa.notes,
    0            AS price,
    fa.created_at,
    s.area_id,
    fa.updated_at,
    fa.id
FROM fitness_appointment fa
JOIN specialist s ON fa.trainer_id = s.id;

-- Step 4: Re-route report FK (no-op in test env — report table is empty)
ALTER TABLE report DROP CONSTRAINT IF EXISTS report_fitness_appointment_id_fkey;
ALTER TABLE report DROP CONSTRAINT IF EXISTS report_fitness_appointment_id_key;
DROP INDEX IF EXISTS idx_report_fitness_appointment_id;

ALTER TABLE report RENAME COLUMN fitness_appointment_id TO appointment_id;

UPDATE report r
SET r.appointment_id = a.id
FROM appointment a
WHERE a._fitness_id = r.appointment_id;

ALTER TABLE report ADD CONSTRAINT report_appointment_id_fkey
    FOREIGN KEY (appointment_id) REFERENCES appointment(id);
ALTER TABLE report ADD CONSTRAINT report_appointment_id_key UNIQUE (appointment_id);

-- Step 5: Cleanup
ALTER TABLE appointment DROP COLUMN _fitness_id;
ALTER TABLE appointment DROP COLUMN visit_type;

DROP INDEX IF EXISTS idx_fitness_appt_patient_id;
DROP INDEX IF EXISTS idx_fitness_appt_trainer_id;
DROP INDEX IF EXISTS idx_fitness_appt_scheduled_at;
DROP INDEX IF EXISTS idx_fitness_appt_status;
DROP TABLE IF EXISTS fitness_appointment;

CREATE INDEX idx_appointment_area_id ON appointment(area_id);
