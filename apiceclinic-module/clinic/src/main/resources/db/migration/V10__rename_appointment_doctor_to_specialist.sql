-- V10: Rename appointment.doctor_id to specialist_id
ALTER TABLE appointment RENAME COLUMN doctor_id TO specialist_id;
DROP INDEX IF EXISTS idx_appointment_doctor_id;
CREATE INDEX idx_appointment_specialist_id ON appointment(specialist_id);
