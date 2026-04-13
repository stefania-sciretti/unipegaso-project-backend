-- ===================================================
-- CentroFitness Simona & Luca - Monitoraggio Glicemia (H2-compatible)
-- V3__health_tests_schema.sql
-- ===================================================

-- ── Tabella GLYCEMIA_MEASUREMENT (Misurazioni Glicemia – pungidito) ──────────
CREATE TABLE IF NOT EXISTS glycemia_measurement (
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    client_id    BIGINT       NOT NULL REFERENCES client(id),
    trainer_id   BIGINT       NOT NULL REFERENCES trainer(id),
    measured_at  TIMESTAMP    NOT NULL,
    value_mg_dl  DECIMAL(6,2) NOT NULL,
    context      VARCHAR(50)  NOT NULL DEFAULT 'FASTING',
    notes        TEXT,
    created_at   TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- ── Dati di esempio: misurazioni glicemia ──
INSERT INTO glycemia_measurement (client_id, trainer_id, measured_at, value_mg_dl, context, notes) VALUES
    (1, 1, DATEADD(DAY, -30, NOW()),  88,  'FASTING',       'Misurazione di controllo a digiuno'),
    (1, 1, DATEADD(DAY, -15, NOW()),  95,  'FASTING',       'Lieve aumento dopo festività'),
    (1, 1, DATEADD(DAY, -5,  NOW()), 140,  'POST_MEAL_2H',  'Post pranzo abbondante – nella norma'),
    (3, 1, DATEADD(DAY, -20, NOW()),  82,  'FASTING',       'Ottimo valore basale'),
    (5, 1, DATEADD(DAY, -10, NOW()), 105,  'FASTING',       'Borderline – monitorare'),
    (5, 1, DATEADD(DAY, -3,  NOW()), 165,  'POST_MEAL_1H',  'Picco glicemico post-pasto ricco di carboidrati');
