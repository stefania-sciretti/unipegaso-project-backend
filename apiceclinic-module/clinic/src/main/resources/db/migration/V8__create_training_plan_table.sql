-- V8: Create training_plan table (patient_id references patient)
CREATE TABLE training_plan (
    id                BIGSERIAL    PRIMARY KEY,
    patient_id        BIGINT       NOT NULL REFERENCES patient(id),
    trainer_id        BIGINT       NOT NULL REFERENCES specialist(id),
    title             VARCHAR(255) NOT NULL,
    description       TEXT,
    weeks             INT,
    sessions_per_week INT,
    active            BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_training_plan_patient_id  ON training_plan(patient_id);
CREATE INDEX idx_training_plan_trainer_id ON training_plan(trainer_id);
CREATE INDEX idx_training_plan_active     ON training_plan(active);
