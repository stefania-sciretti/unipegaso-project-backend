-- V110: Fix appointment.service_type to use actual service names from the services table.
-- V108 seeded appointments with generic codes (PERSONAL_TRAINING, NUTRITION_PLAN, etc.).
-- This migration replaces them with the real 17 service names and corrects prices accordingly.
-- All 17 services defined in V106_1 are covered at least once.

UPDATE appointment a
SET service_type = nv.service_type,
    price        = nv.price
FROM (
    SELECT
        p.id  AS patient_id,
        s.id  AS specialist_id,
        nv.scheduled_at,
        nv.service_type,
        nv.price
    FROM (VALUES
        -- ── Luca Siretta (PERSONAL_TRAINER) ─────────────────────────────────
        ('a.russo@gmail.com',    'l.siretta@apiceclinic.it', TIMESTAMP '2025-05-10 09:00:00', 'Valutazione posturale e funzionale',          70.00::NUMERIC),
        ('g.marino@gmail.com',   'l.siretta@apiceclinic.it', TIMESTAMP '2025-06-03 09:00:00', 'Seduta di personal training (60 min)',         50.00::NUMERIC),
        ('e.ricci@yahoo.it',     'l.siretta@apiceclinic.it', TIMESTAMP '2025-07-08 09:00:00', 'Seduta di personal training (60 min)',         50.00::NUMERIC),
        ('m.colombo@gmail.com',  'l.siretta@apiceclinic.it', TIMESTAMP '2025-08-12 09:30:00', 'Seduta di personal training (60 min)',         50.00::NUMERIC),
        ('l.gallo@outlook.com',  'l.siretta@apiceclinic.it', TIMESTAMP '2025-09-16 09:00:00', 'Programma di allenamento personalizzato',      80.00::NUMERIC),
        ('r.esposito@libero.it', 'l.siretta@apiceclinic.it', TIMESTAMP '2025-11-04 09:00:00', 'Seduta di personal training (60 min)',         50.00::NUMERIC),
        ('m.bruno@gmail.com',    'l.siretta@apiceclinic.it', TIMESTAMP '2026-01-13 09:00:00', 'Seduta di personal training (60 min)',         50.00::NUMERIC),
        ('a.russo@gmail.com',    'l.siretta@apiceclinic.it', TIMESTAMP '2026-05-08 09:00:00', 'Programma di allenamento personalizzato',      80.00::NUMERIC),
        ('l.ricci@libero.it',    'l.siretta@apiceclinic.it', TIMESTAMP '2026-05-14 09:00:00', 'Seduta di personal training (60 min)',         50.00::NUMERIC),
        ('s.conti@libero.it',    'l.siretta@apiceclinic.it', TIMESTAMP '2026-05-21 09:30:00', 'Pacchetto 10 sedute personal training',       450.00::NUMERIC),
        ('d.romano@yahoo.it',    'l.siretta@apiceclinic.it', TIMESTAMP '2026-05-29 09:00:00', 'Programma di allenamento personalizzato',      80.00::NUMERIC),
        -- ── Simona Ruberti (NUTRITIONIST) ───────────────────────────────────
        ('r.esposito@libero.it', 's.ruberti@apiceclinic.it', TIMESTAMP '2025-05-14 10:30:00', 'Visita nutrizionale iniziale',                90.00::NUMERIC),
        ('m.bruno@gmail.com',    's.ruberti@apiceclinic.it', TIMESTAMP '2025-06-10 10:00:00', 'Consulenza nutrizionale di follow-up',        60.00::NUMERIC),
        ('d.romano@yahoo.it',    's.ruberti@apiceclinic.it', TIMESTAMP '2025-08-05 10:00:00', 'Piano alimentare personalizzato',            120.00::NUMERIC),
        ('s.conti@libero.it',    's.ruberti@apiceclinic.it', TIMESTAMP '2025-09-23 10:00:00', 'Piano alimentare personalizzato',            120.00::NUMERIC),
        ('d.romano@yahoo.it',    's.ruberti@apiceclinic.it', TIMESTAMP '2025-11-11 10:30:00', 'Consulenza nutrizionale di follow-up',        60.00::NUMERIC),
        ('l.gallo@outlook.com',  's.ruberti@apiceclinic.it', TIMESTAMP '2026-01-20 10:00:00', 'Consulenza nutrizionale di follow-up',        60.00::NUMERIC),
        ('r.esposito@libero.it', 's.ruberti@apiceclinic.it', TIMESTAMP '2026-05-09 10:30:00', 'Consulenza nutrizionale di follow-up',        60.00::NUMERIC),
        ('g.marino@gmail.com',   's.ruberti@apiceclinic.it', TIMESTAMP '2026-05-15 10:00:00', 'Visita nutrizionale iniziale',                90.00::NUMERIC),
        ('e.ricci@yahoo.it',     's.ruberti@apiceclinic.it', TIMESTAMP '2026-05-26 10:00:00', 'Visita nutrizionale iniziale',                90.00::NUMERIC),
        -- ── Sandro Scironi (SPORT_DOCTOR) ───────────────────────────────────
        ('d.romano@yahoo.it',    's.scironi@apiceclinic.it', TIMESTAMP '2025-05-20 08:00:00', 'Visita medico-sportiva',                     120.00::NUMERIC),
        ('l.gallo@outlook.com',  's.scironi@apiceclinic.it', TIMESTAMP '2025-06-17 08:30:00', 'Visita medico-sportiva',                     120.00::NUMERIC),
        ('l.ricci@libero.it',    's.scironi@apiceclinic.it', TIMESTAMP '2025-08-19 08:00:00', 'Certificato idoneità sportiva agonistica',   150.00::NUMERIC),
        ('s.colombo@gmail.com',  's.scironi@apiceclinic.it', TIMESTAMP '2025-10-07 08:00:00', 'Visita medico-sportiva',                     120.00::NUMERIC),
        ('m.colombo@gmail.com',  's.scironi@apiceclinic.it', TIMESTAMP '2025-11-18 08:00:00', 'Certificato idoneità sportiva agonistica',   150.00::NUMERIC),
        ('s.conti@libero.it',    's.scironi@apiceclinic.it', TIMESTAMP '2026-05-05 08:00:00', 'Visita medico-sportiva',                     120.00::NUMERIC),
        ('d.romano@yahoo.it',    's.scironi@apiceclinic.it', TIMESTAMP '2026-05-12 08:30:00', 'Visita medico-sportiva',                     120.00::NUMERIC),
        ('l.gallo@outlook.com',  's.scironi@apiceclinic.it', TIMESTAMP '2026-05-20 08:00:00', 'Elettrocardiogramma sotto sforzo',           200.00::NUMERIC),
        ('m.colombo@gmail.com',  's.scironi@apiceclinic.it', TIMESTAMP '2026-06-02 08:00:00', 'Certificato idoneità sportiva agonistica',   150.00::NUMERIC),
        ('g.marino@gmail.com',   's.scironi@apiceclinic.it', TIMESTAMP '2026-06-04 08:30:00', 'Certificato idoneità sportiva agonistica',   150.00::NUMERIC),
        -- ── Cristiana Maratti (DIETOLOGIST) ─────────────────────────────────
        ('m.colombo@gmail.com',  'c.maratti@apiceclinic.it', TIMESTAMP '2025-05-22 11:00:00', 'Prima visita dietologica',                   110.00::NUMERIC),
        ('s.conti@libero.it',    'c.maratti@apiceclinic.it', TIMESTAMP '2025-06-24 11:00:00', 'Controllo e aggiornamento piano dietetico',   70.00::NUMERIC),
        ('a.russo@gmail.com',    'c.maratti@apiceclinic.it', TIMESTAMP '2025-07-15 10:30:00', 'Controllo e aggiornamento piano dietetico',   70.00::NUMERIC),
        ('g.marino@gmail.com',   'c.maratti@apiceclinic.it', TIMESTAMP '2025-09-02 11:00:00', 'Controllo e aggiornamento piano dietetico',   70.00::NUMERIC),
        ('e.ricci@yahoo.it',     'c.maratti@apiceclinic.it', TIMESTAMP '2025-10-14 11:00:00', 'Prima visita dietologica',                   110.00::NUMERIC),
        ('l.ricci@libero.it',    'c.maratti@apiceclinic.it', TIMESTAMP '2025-12-02 11:00:00', 'Controllo e aggiornamento piano dietetico',   70.00::NUMERIC),
        ('s.colombo@gmail.com',  'c.maratti@apiceclinic.it', TIMESTAMP '2026-05-06 11:00:00', 'Prima visita dietologica',                   110.00::NUMERIC),
        ('m.bruno@gmail.com',    'c.maratti@apiceclinic.it', TIMESTAMP '2026-05-19 11:00:00', 'Piano dietetico per disturbi metabolici',    150.00::NUMERIC),
        ('a.russo@gmail.com',    'c.maratti@apiceclinic.it', TIMESTAMP '2026-05-27 11:00:00', 'Controllo e aggiornamento piano dietetico',   70.00::NUMERIC),
        ('l.ricci@libero.it',    'c.maratti@apiceclinic.it', TIMESTAMP '2026-06-03 11:30:00', 'Prima visita dietologica',                   110.00::NUMERIC),
        -- ── Michele Lavori (PHYSIOTHERAPIST) ────────────────────────────────
        ('l.ricci@libero.it',    'm.lavori@apiceclinic.it',  TIMESTAMP '2025-05-28 14:00:00', 'Seduta di fisioterapia (45 min)',             55.00::NUMERIC),
        ('s.colombo@gmail.com',  'm.lavori@apiceclinic.it',  TIMESTAMP '2025-07-01 15:00:00', 'Valutazione fisioterapica',                   80.00::NUMERIC),
        ('r.esposito@libero.it', 'm.lavori@apiceclinic.it',  TIMESTAMP '2025-07-22 14:00:00', 'Trattamento manuale ortopedico',              65.00::NUMERIC),
        ('m.bruno@gmail.com',    'm.lavori@apiceclinic.it',  TIMESTAMP '2025-09-09 14:30:00', 'Trattamento manuale ortopedico',              65.00::NUMERIC),
        ('a.russo@gmail.com',    'm.lavori@apiceclinic.it',  TIMESTAMP '2025-10-21 14:00:00', 'Seduta di fisioterapia (45 min)',             55.00::NUMERIC),
        ('g.marino@gmail.com',   'm.lavori@apiceclinic.it',  TIMESTAMP '2025-12-09 15:00:00', 'Seduta di fisioterapia (45 min)',             55.00::NUMERIC),
        ('e.ricci@yahoo.it',     'm.lavori@apiceclinic.it',  TIMESTAMP '2026-05-07 14:00:00', 'Valutazione fisioterapica',                   80.00::NUMERIC),
        ('m.colombo@gmail.com',  'm.lavori@apiceclinic.it',  TIMESTAMP '2026-05-13 14:30:00', 'Trattamento manuale ortopedico',              65.00::NUMERIC),
        ('s.colombo@gmail.com',  'm.lavori@apiceclinic.it',  TIMESTAMP '2026-05-22 15:00:00', 'Ciclo riabilitativo post-operatorio (10 sedute)', 500.00::NUMERIC),
        ('r.esposito@libero.it', 'm.lavori@apiceclinic.it',  TIMESTAMP '2026-05-28 14:00:00', 'Seduta di fisioterapia (45 min)',             55.00::NUMERIC)
    ) AS nv(patient_email, specialist_email, scheduled_at, service_type, price)
    JOIN patient    p ON p.email = nv.patient_email
    JOIN specialist s ON s.email = nv.specialist_email
) AS nv
WHERE a.patient_id    = nv.patient_id
  AND a.specialist_id = nv.specialist_id
  AND a.scheduled_at  = nv.scheduled_at;
