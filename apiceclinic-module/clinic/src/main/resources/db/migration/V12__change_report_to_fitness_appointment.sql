-- V12: Change report to reference fitness_appointment instead of appointment
DELETE FROM report;
ALTER TABLE report DROP CONSTRAINT IF EXISTS report_appointment_id_fkey;
ALTER TABLE report DROP CONSTRAINT IF EXISTS report_appointment_id_key;
DROP INDEX IF EXISTS idx_report_appointment_id;
ALTER TABLE report RENAME COLUMN appointment_id TO fitness_appointment_id;
ALTER TABLE report ADD CONSTRAINT report_fitness_appointment_id_fkey
    FOREIGN KEY (fitness_appointment_id) REFERENCES fitness_appointment(id);
ALTER TABLE report ADD CONSTRAINT report_fitness_appointment_id_key
    UNIQUE (fitness_appointment_id);
CREATE INDEX idx_report_fitness_appointment_id ON report(fitness_appointment_id);
