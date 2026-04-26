-- Initial schema for Apice Clinic core tables
-- Tables: specialist, patient, appointment, report

CREATE TABLE IF NOT EXISTS specialist (
    id              BIGSERIAL PRIMARY KEY,
    first_name      VARCHAR(100)        NOT NULL,
    last_name       VARCHAR(100)        NOT NULL,
    specialization  VARCHAR(200)        NOT NULL,
    email           VARCHAR(255)        NOT NULL UNIQUE,
    license_number  VARCHAR(50)         NOT NULL UNIQUE,
    created_at      TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_specialist_id ON specialist(id);

CREATE TABLE IF NOT EXISTS patient (
    id          BIGSERIAL PRIMARY KEY,
    first_name  VARCHAR(100)    NOT NULL,
    last_name   VARCHAR(100)    NOT NULL,
    fiscal_code VARCHAR(16)     NOT NULL UNIQUE,
    birth_date  DATE            NOT NULL,
    email       VARCHAR(255)    NOT NULL,
    phone       VARCHAR(50),
    updated_at  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_patient_id ON patient(id);

CREATE TABLE IF NOT EXISTS appointment (
    id           BIGSERIAL PRIMARY KEY,
    patient_id   BIGINT          NOT NULL REFERENCES patient(id),
    specialist_id    BIGINT          NOT NULL REFERENCES specialist(id),
    scheduled_at TIMESTAMP       NOT NULL,
    visit_type   VARCHAR(200)    NOT NULL,
    status       VARCHAR(20)     NOT NULL CHECK (status IN ('BOOKED', 'CONFIRMED', 'COMPLETED', 'CANCELLED')),
    notes        TEXT,
    updated_at   TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_appointment_patient_id ON appointment(patient_id);
CREATE INDEX IF NOT EXISTS idx_appointment_specialist_id  ON appointment(specialist_id);
CREATE INDEX IF NOT EXISTS idx_appointment_status     ON appointment(status);

CREATE TABLE IF NOT EXISTS report (
    id               BIGSERIAL PRIMARY KEY,
    appointment_id   BIGINT      NOT NULL UNIQUE REFERENCES appointment(id),
    issued_date      DATE        NOT NULL DEFAULT CURRENT_DATE,
    diagnosis        TEXT        NOT NULL,
    prescription     TEXT,
    specialist_notes     TEXT,
    created_at       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_report_appointment_id ON report(appointment_id);
