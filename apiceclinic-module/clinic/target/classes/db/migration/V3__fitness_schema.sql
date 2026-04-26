-- Fitness / wellness domain tables
-- Tables: fitness_appointment, diet_plan, training_plan, recipe

CREATE TABLE IF NOT EXISTS fitness_appointment (
    id           BIGSERIAL PRIMARY KEY,
    patient_id   BIGINT          NOT NULL REFERENCES patient(id),
    specialist_id BIGINT          NOT NULL REFERENCES specialist(id),
    scheduled_at TIMESTAMP       NOT NULL,
    service_type VARCHAR(200)    NOT NULL,
    status       VARCHAR(20)     NOT NULL DEFAULT 'BOOKED' CHECK (status IN ('BOOKED', 'CONFIRMED', 'COMPLETED', 'CANCELLED')),
    notes        TEXT,
    created_at   TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_fitness_appointment_patient_id ON fitness_appointment(patient_id);
CREATE INDEX IF NOT EXISTS idx_fitness_appointment_specialist_id ON fitness_appointment(specialist_id);
CREATE INDEX IF NOT EXISTS idx_fitness_appointment_status     ON fitness_appointment(status);

CREATE TABLE IF NOT EXISTS diet_plan (
    id             BIGSERIAL PRIMARY KEY,
    patient_id     BIGINT          NOT NULL REFERENCES patient(id),
    specialist_id  BIGINT          NOT NULL REFERENCES specialist(id),
    title          VARCHAR(255)    NOT NULL,
    description    TEXT,
    calories       INTEGER,
    duration_weeks INTEGER,
    active         BOOLEAN         NOT NULL DEFAULT TRUE,
    created_at     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_diet_plan_patient_id ON diet_plan(patient_id);
CREATE INDEX IF NOT EXISTS idx_diet_plan_active     ON diet_plan(active);

CREATE TABLE IF NOT EXISTS training_plan (
    id                BIGSERIAL PRIMARY KEY,
    patient_id        BIGINT          NOT NULL REFERENCES patient(id),
    specialist_id     BIGINT          NOT NULL REFERENCES specialist(id),
    title             VARCHAR(255)    NOT NULL,
    description       TEXT,
    weeks             INTEGER,
    sessions_per_week INTEGER,
    active            BOOLEAN         NOT NULL DEFAULT TRUE,
    created_at        TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_training_plan_patient_id ON training_plan(patient_id);
CREATE INDEX IF NOT EXISTS idx_training_plan_active     ON training_plan(active);

CREATE TABLE IF NOT EXISTS recipe (
    id           BIGSERIAL PRIMARY KEY,
    title        VARCHAR(255)    NOT NULL,
    description  TEXT,
    ingredients  TEXT,
    instructions TEXT,
    calories     INTEGER,
    category     VARCHAR(100),
    created_at   TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_recipe_category ON recipe(category);
