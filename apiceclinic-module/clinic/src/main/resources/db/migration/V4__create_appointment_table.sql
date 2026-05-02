-- V4: Create appointment table
CREATE TABLE appointment (
    id           BIGSERIAL    PRIMARY KEY,
    patient_id   BIGINT       NOT NULL REFERENCES patient(id),
    specialist_id    BIGINT       NOT NULL REFERENCES specialist(id),
    scheduled_at TIMESTAMP    NOT NULL,
    visit_type   VARCHAR(200) NOT NULL,
    status       VARCHAR(20)  NOT NULL DEFAULT 'BOOKED',
    notes        TEXT,
    updated_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_appointment_patient_id   ON appointment(patient_id);
CREATE INDEX idx_appointment_specialist_id    ON appointment(specialist_id);
CREATE INDEX idx_appointment_scheduled_at ON appointment(scheduled_at);
CREATE INDEX idx_appointment_status       ON appointment(status);
