-- ===================================================
-- CentroFitness Simona & Luca - Monitoraggio Glicemia
-- V3__health_tests_schema.sql
-- ===================================================

-- ── Tabella GLYCEMIA_MEASUREMENT (Misurazioni Glicemia – pungidito) ──────────
CREATE TABLE glycemia_measurement (
    id           BIGSERIAL    PRIMARY KEY,
    client_id    BIGINT       NOT NULL REFERENCES client(id) ON DELETE RESTRICT,
    trainer_id   BIGINT       NOT NULL REFERENCES trainer(id) ON DELETE RESTRICT,
    measured_at  TIMESTAMP    NOT NULL,
    value_mg_dl  NUMERIC(6,2) NOT NULL,   -- valore in mg/dL
    context      VARCHAR(50)  NOT NULL DEFAULT 'FASTING',
                 -- FASTING | POST_MEAL_1H | POST_MEAL_2H | RANDOM
    notes        TEXT,
    created_at   TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Indici
CREATE INDEX idx_glycemia_client   ON glycemia_measurement(client_id);
CREATE INDEX idx_glycemia_measured ON glycemia_measurement(measured_at);

-- ── Dati di esempio: misurazioni glicemia ──
INSERT INTO glycemia_measurement (client_id, trainer_id, measured_at, value_mg_dl, context, notes) VALUES
    (1, 1, NOW() - INTERVAL '30 days',  88,  'FASTING',       'Misurazione di controllo a digiuno'),
    (1, 1, NOW() - INTERVAL '15 days',  95,  'FASTING',       'Lieve aumento dopo festività'),
    (1, 1, NOW() - INTERVAL '5 days',   140, 'POST_MEAL_2H',  'Post pranzo abbondante – nella norma'),
    (3, 1, NOW() - INTERVAL '20 days',  82,  'FASTING',       'Ottimo valore basale'),
    (5, 1, NOW() - INTERVAL '10 days',  105, 'FASTING',       'Borderline – monitorare'),
    (5, 1, NOW() - INTERVAL '3 days',   165, 'POST_MEAL_1H',  'Picco glicemico post-pasto ricco di carboidrati');

CREATE TABLE glycemia_measurement (
    id           BIGSERIAL    PRIMARY KEY,
    client_id    BIGINT       NOT NULL REFERENCES client(id) ON DELETE RESTRICT,
    trainer_id   BIGINT       NOT NULL REFERENCES trainer(id) ON DELETE RESTRICT,
    measured_at  TIMESTAMP    NOT NULL,
    value_mg_dl  NUMERIC(6,2) NOT NULL,   -- valore in mg/dL
    context      VARCHAR(50)  NOT NULL DEFAULT 'FASTING',
                 -- FASTING | POST_MEAL_1H | POST_MEAL_2H | RANDOM
    notes        TEXT,
    created_at   TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Indici
CREATE INDEX idx_intol_test_client  ON intolerance_test(client_id);
CREATE INDEX idx_intol_test_date    ON intolerance_test(test_date);
CREATE INDEX idx_glycemia_client    ON glycemia_measurement(client_id);
CREATE INDEX idx_glycemia_measured  ON glycemia_measurement(measured_at);

-- ── Dati di esempio: test intolleranze ──
INSERT INTO intolerance_test (client_id, trainer_id, test_type, test_date, result_summary, intolerances, recommendations) VALUES
    (1, 1, 'Cytotest 200 alimenti', '2025-09-10',
     'Intolleranza moderata a frumento e latticini. Sensibilità lieve a uova.',
     'Frumento, Latticini vaccini, Uova',
     'Eliminare il frumento per 3 mesi. Preferire latte vegetale. Uova max 3/settimana.'),
    (3, 1, 'Cytotest 200 alimenti', '2025-10-05',
     'Nessuna intolleranza rilevante. Lieve sensibilità al lievito di birra.',
     'Lievito di birra',
     'Ridurre prodotti lievitati. Piano alimentare già ben bilanciato.'),
    (5, 1, 'Test intolleranze base', '2025-11-20',
     'Intolleranza al nichel e sensibilità al glutine (non celiaca).',
     'Nichel, Glutine (sensibilità)',
     'Dieta a basso contenuto di nichel. Ridurre glutine e monitorare sintomi.');

-- ── Dati di esempio: misurazioni glicemia ──
INSERT INTO glycemia_measurement (client_id, trainer_id, measured_at, value_mg_dl, context, notes) VALUES
    (1, 1, NOW() - INTERVAL '30 days',  88,  'FASTING',       'Misurazione di controllo a digiuno'),
    (1, 1, NOW() - INTERVAL '15 days',  95,  'FASTING',       'Lieve aumento dopo festività'),
    (1, 1, NOW() - INTERVAL '5 days',   140, 'POST_MEAL_2H',  'Post pranzo abbondante – nella norma'),
    (3, 1, NOW() - INTERVAL '20 days',  82,  'FASTING',       'Ottimo valore basale'),
    (5, 1, NOW() - INTERVAL '10 days',  105, 'FASTING',       'Borderline – monitorare'),
    (5, 1, NOW() - INTERVAL '3 days',   165, 'POST_MEAL_1H',  'Picco glicemico post-pasto ricco di carboidrati');
