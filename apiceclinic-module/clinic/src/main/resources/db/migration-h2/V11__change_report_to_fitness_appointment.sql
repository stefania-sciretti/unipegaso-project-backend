-- V11 H2: Change report to reference fitness_appointment instead of appointment
DELETE FROM report;
ALTER TABLE report DROP CONSTRAINT IF EXISTS constraint_report_appointment_id;
DROP INDEX IF EXISTS idx_report_appointment_id;
ALTER TABLE report DROP COLUMN appointment_id;
ALTER TABLE report ADD COLUMN fitness_appointment_id BIGINT NOT NULL DEFAULT 0;
ALTER TABLE report ADD CONSTRAINT report_fitness_appointment_id_key UNIQUE (fitness_appointment_id);
ALTER TABLE report ADD CONSTRAINT report_fitness_appointment_id_fkey
    FOREIGN KEY (fitness_appointment_id) REFERENCES fitness_appointment(id);
ALTER TABLE report ALTER COLUMN fitness_appointment_id DROP DEFAULT;
CREATE INDEX idx_report_fitness_appointment_id ON report(fitness_appointment_id);
