-- V101: Create glycemia_measurement table
CREATE TABLE glycemia_measurement (
    id           BIGSERIAL    PRIMARY KEY,
    patient_id   BIGINT       NOT NULL REFERENCES patient(id),
    specialist_id BIGINT      NOT NULL REFERENCES specialist(id),
    measured_at  TIMESTAMP    NOT NULL,
    value_mg_dl  INT          NOT NULL,
    context      VARCHAR(20)  NOT NULL,
    notes        TEXT,
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_glycemia_patient_id   ON glycemia_measurement(patient_id);
CREATE INDEX idx_glycemia_measured_at  ON glycemia_measurement(measured_at);
CREATE INDEX idx_glycemia_context      ON glycemia_measurement(context);
