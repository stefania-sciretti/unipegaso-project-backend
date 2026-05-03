-- V108: Seed 50 realistic appointments (May 2025 – May 2026)

-- Prerequisite check: fail loudly if required patients/specialists are missing
DO $$
BEGIN
    IF (SELECT COUNT(*) FROM patient WHERE email IN (
        'a.russo@gmail.com','r.esposito@libero.it','d.romano@yahoo.it','m.colombo@gmail.com',
        'l.ricci@libero.it','g.marino@gmail.com','m.bruno@gmail.com','l.gallo@outlook.com',
        's.conti@libero.it','s.colombo@gmail.com','e.ricci@yahoo.it'
    )) <> 11 THEN
        RAISE EXCEPTION 'V108: prerequisite check failed – not all 11 patient emails found';
    END IF;
    IF (SELECT COUNT(*) FROM specialist WHERE email IN (
        'l.siretta@apiceclinic.it','s.ruberti@apiceclinic.it','s.scironi@apiceclinic.it',
        'c.maratti@apiceclinic.it','m.lavori@apiceclinic.it'
    ) AND area_id IS NOT NULL) <> 5 THEN
        RAISE EXCEPTION 'V108: prerequisite check failed – not all 5 specialist emails found or area_id is NULL';
    END IF;
END $$;

INSERT INTO appointment (patient_id, specialist_id, scheduled_at, service_type, status, notes, price, created_at, area_id, updated_at)
SELECT
    p.id,
    s.id,
    appt.scheduled_at,
    appt.service_type,
    appt.status,
    appt.notes,
    appt.price,
    appt.scheduled_at - INTERVAL '1 day',
    s.area_id,
    CURRENT_TIMESTAMP
FROM (VALUES
  ('a.russo@gmail.com',     'l.siretta@apiceclinic.it', TIMESTAMP '2025-05-10 09:00:00', 'PERSONAL_TRAINING',   'COMPLETED', 'Prima sessione valutativa',         60.00::NUMERIC),
  ('r.esposito@libero.it',  's.ruberti@apiceclinic.it', TIMESTAMP '2025-05-14 10:30:00', 'NUTRITION_PLAN',      'COMPLETED', 'Piano alimentare iniziale',         80.00::NUMERIC),
  ('d.romano@yahoo.it',     's.scironi@apiceclinic.it', TIMESTAMP '2025-05-20 08:00:00', 'SPORT_MEDICAL_VISIT', 'COMPLETED', 'Visita idoneita sportiva',         100.00::NUMERIC),
  ('m.colombo@gmail.com',   'c.maratti@apiceclinic.it', TIMESTAMP '2025-05-22 11:00:00', 'DIET_CONSULTATION',   'COMPLETED', 'Consulenza dietetica metabolica',   90.00::NUMERIC),
  ('l.ricci@libero.it',     'm.lavori@apiceclinic.it',  TIMESTAMP '2025-05-28 14:00:00', 'PHYSIOTHERAPY',       'COMPLETED', 'Fisioterapia spalla destra',        70.00::NUMERIC),
  ('g.marino@gmail.com',    'l.siretta@apiceclinic.it', TIMESTAMP '2025-06-03 09:00:00', 'PERSONAL_TRAINING',   'COMPLETED', 'Allenamento forza funzionale',      60.00::NUMERIC),
  ('m.bruno@gmail.com',     's.ruberti@apiceclinic.it', TIMESTAMP '2025-06-10 10:00:00', 'NUTRITION_PLAN',      'COMPLETED', 'Revisione piano alimentare',        80.00::NUMERIC),
  ('l.gallo@outlook.com',   's.scironi@apiceclinic.it', TIMESTAMP '2025-06-17 08:30:00', 'SPORT_MEDICAL_VISIT', 'COMPLETED', 'Controllo idoneita annuale',       100.00::NUMERIC),
  ('s.conti@libero.it',     'c.maratti@apiceclinic.it', TIMESTAMP '2025-06-24 11:00:00', 'DIET_CONSULTATION',   'COMPLETED', 'Follow-up piano dietetico',         90.00::NUMERIC),
  ('s.colombo@gmail.com',   'm.lavori@apiceclinic.it',  TIMESTAMP '2025-07-01 15:00:00', 'PHYSIOTHERAPY',       'COMPLETED', 'Riabilitazione ginocchio',          70.00::NUMERIC),
  ('e.ricci@yahoo.it',      'l.siretta@apiceclinic.it', TIMESTAMP '2025-07-08 09:00:00', 'PERSONAL_TRAINING',   'COMPLETED', 'Allenamento resistenza',            60.00::NUMERIC),
  ('a.russo@gmail.com',     'c.maratti@apiceclinic.it', TIMESTAMP '2025-07-15 10:30:00', 'DIET_CONSULTATION',   'COMPLETED', 'Monitoraggio peso corporeo',        90.00::NUMERIC),
  ('r.esposito@libero.it',  'm.lavori@apiceclinic.it',  TIMESTAMP '2025-07-22 14:00:00', 'PHYSIOTHERAPY',       'COMPLETED', 'Trattamento lombalgia',             70.00::NUMERIC),
  ('d.romano@yahoo.it',     's.ruberti@apiceclinic.it', TIMESTAMP '2025-08-05 10:00:00', 'NUTRITION_PLAN',      'COMPLETED', 'Piano per perdita peso',            80.00::NUMERIC),
  ('m.colombo@gmail.com',   'l.siretta@apiceclinic.it', TIMESTAMP '2025-08-12 09:30:00', 'PERSONAL_TRAINING',   'COMPLETED', 'Condizionamento fisico estivo',     60.00::NUMERIC),
  ('l.ricci@libero.it',     's.scironi@apiceclinic.it', TIMESTAMP '2025-08-19 08:00:00', 'SPORT_MEDICAL_VISIT', 'COMPLETED', 'Visita pre-agonistica',            100.00::NUMERIC),
  ('g.marino@gmail.com',    'c.maratti@apiceclinic.it', TIMESTAMP '2025-09-02 11:00:00', 'DIET_CONSULTATION',   'COMPLETED', 'Adattamento dieta autunnale',       90.00::NUMERIC),
  ('m.bruno@gmail.com',     'm.lavori@apiceclinic.it',  TIMESTAMP '2025-09-09 14:30:00', 'PHYSIOTHERAPY',       'COMPLETED', 'Terapia manuale cervicale',         70.00::NUMERIC),
  ('l.gallo@outlook.com',   'l.siretta@apiceclinic.it', TIMESTAMP '2025-09-16 09:00:00', 'PERSONAL_TRAINING',   'COMPLETED', 'Allenamento ipertrofia',            60.00::NUMERIC),
  ('s.conti@libero.it',     's.ruberti@apiceclinic.it', TIMESTAMP '2025-09-23 10:00:00', 'NUTRITION_PLAN',      'COMPLETED', 'Piano dieta ipocalorica',           80.00::NUMERIC),
  ('s.colombo@gmail.com',   's.scironi@apiceclinic.it', TIMESTAMP '2025-10-07 08:00:00', 'SPORT_MEDICAL_VISIT', 'CANCELLED', NULL,                               100.00::NUMERIC),
  ('e.ricci@yahoo.it',      'c.maratti@apiceclinic.it', TIMESTAMP '2025-10-14 11:00:00', 'DIET_CONSULTATION',   'CANCELLED', NULL,                                90.00::NUMERIC),
  ('a.russo@gmail.com',     'm.lavori@apiceclinic.it',  TIMESTAMP '2025-10-21 14:00:00', 'PHYSIOTHERAPY',       'CANCELLED', NULL,                                70.00::NUMERIC),
  ('r.esposito@libero.it',  'l.siretta@apiceclinic.it', TIMESTAMP '2025-11-04 09:00:00', 'PERSONAL_TRAINING',   'CANCELLED', NULL,                                60.00::NUMERIC),
  ('d.romano@yahoo.it',     's.ruberti@apiceclinic.it', TIMESTAMP '2025-11-11 10:30:00', 'NUTRITION_PLAN',      'CANCELLED', NULL,                                80.00::NUMERIC),
  ('m.colombo@gmail.com',   's.scironi@apiceclinic.it', TIMESTAMP '2025-11-18 08:00:00', 'SPORT_MEDICAL_VISIT', 'CANCELLED', NULL,                               100.00::NUMERIC),
  ('l.ricci@libero.it',     'c.maratti@apiceclinic.it', TIMESTAMP '2025-12-02 11:00:00', 'DIET_CONSULTATION',   'CANCELLED', NULL,                                90.00::NUMERIC),
  ('g.marino@gmail.com',    'm.lavori@apiceclinic.it',  TIMESTAMP '2025-12-09 15:00:00', 'PHYSIOTHERAPY',       'CANCELLED', NULL,                                70.00::NUMERIC),
  ('m.bruno@gmail.com',     'l.siretta@apiceclinic.it', TIMESTAMP '2026-01-13 09:00:00', 'PERSONAL_TRAINING',   'CANCELLED', NULL,                                60.00::NUMERIC),
  ('l.gallo@outlook.com',   's.ruberti@apiceclinic.it', TIMESTAMP '2026-01-20 10:00:00', 'NUTRITION_PLAN',      'CANCELLED', NULL,                                80.00::NUMERIC),
  ('s.conti@libero.it',     's.scironi@apiceclinic.it', TIMESTAMP '2026-05-05 08:00:00', 'SPORT_MEDICAL_VISIT', 'BOOKED',    'Visita medica sportiva annuale',   100.00::NUMERIC),
  ('s.colombo@gmail.com',   'c.maratti@apiceclinic.it', TIMESTAMP '2026-05-06 11:00:00', 'DIET_CONSULTATION',   'BOOKED',    'Prima consulenza dietetica',        90.00::NUMERIC),
  ('e.ricci@yahoo.it',      'm.lavori@apiceclinic.it',  TIMESTAMP '2026-05-07 14:00:00', 'PHYSIOTHERAPY',       'BOOKED',    'Valutazione posturale',             70.00::NUMERIC),
  ('a.russo@gmail.com',     'l.siretta@apiceclinic.it', TIMESTAMP '2026-05-08 09:00:00', 'PERSONAL_TRAINING',   'BOOKED',    'Nuovo ciclo allenamento',           60.00::NUMERIC),
  ('r.esposito@libero.it',  's.ruberti@apiceclinic.it', TIMESTAMP '2026-05-09 10:30:00', 'NUTRITION_PLAN',      'BOOKED',    'Aggiornamento piano alimentare',    80.00::NUMERIC),
  ('d.romano@yahoo.it',     's.scironi@apiceclinic.it', TIMESTAMP '2026-05-12 08:30:00', 'SPORT_MEDICAL_VISIT', 'BOOKED',    NULL,                               100.00::NUMERIC),
  ('m.colombo@gmail.com',   'm.lavori@apiceclinic.it',  TIMESTAMP '2026-05-13 14:30:00', 'PHYSIOTHERAPY',       'BOOKED',    'Trattamento tendinite',             70.00::NUMERIC),
  ('l.ricci@libero.it',     'l.siretta@apiceclinic.it', TIMESTAMP '2026-05-14 09:00:00', 'PERSONAL_TRAINING',   'BOOKED',    NULL,                                60.00::NUMERIC),
  ('g.marino@gmail.com',    's.ruberti@apiceclinic.it', TIMESTAMP '2026-05-15 10:00:00', 'NUTRITION_PLAN',      'BOOKED',    'Consulenza nutrizionale sportiva',   80.00::NUMERIC),
  ('m.bruno@gmail.com',     'c.maratti@apiceclinic.it', TIMESTAMP '2026-05-19 11:00:00', 'DIET_CONSULTATION',   'BOOKED',    NULL,                                90.00::NUMERIC),
  ('l.gallo@outlook.com',   's.scironi@apiceclinic.it', TIMESTAMP '2026-05-20 08:00:00', 'SPORT_MEDICAL_VISIT', 'BOOKED',    'Controllo cardiologico sportivo',  100.00::NUMERIC),
  ('s.conti@libero.it',     'l.siretta@apiceclinic.it', TIMESTAMP '2026-05-21 09:30:00', 'PERSONAL_TRAINING',   'BOOKED',    NULL,                                60.00::NUMERIC),
  ('s.colombo@gmail.com',   'm.lavori@apiceclinic.it',  TIMESTAMP '2026-05-22 15:00:00', 'PHYSIOTHERAPY',       'BOOKED',    'Riabilitazione post-operatoria',    70.00::NUMERIC),
  ('e.ricci@yahoo.it',      's.ruberti@apiceclinic.it', TIMESTAMP '2026-05-26 10:00:00', 'NUTRITION_PLAN',      'BOOKED',    NULL,                                80.00::NUMERIC),
  ('a.russo@gmail.com',     'c.maratti@apiceclinic.it', TIMESTAMP '2026-05-27 11:00:00', 'DIET_CONSULTATION',   'BOOKED',    'Monitoraggio composizione corporea', 90.00::NUMERIC),
  ('r.esposito@libero.it',  'm.lavori@apiceclinic.it',  TIMESTAMP '2026-05-28 14:00:00', 'PHYSIOTHERAPY',       'BOOKED',    NULL,                                70.00::NUMERIC),
  ('d.romano@yahoo.it',     'l.siretta@apiceclinic.it', TIMESTAMP '2026-05-29 09:00:00', 'PERSONAL_TRAINING',   'BOOKED',    'Sessione forza avanzata',           60.00::NUMERIC),
  ('m.colombo@gmail.com',   's.scironi@apiceclinic.it', TIMESTAMP '2026-06-02 08:00:00', 'SPORT_MEDICAL_VISIT', 'BOOKED',    NULL,                               100.00::NUMERIC),
  ('l.ricci@libero.it',     'c.maratti@apiceclinic.it', TIMESTAMP '2026-06-03 11:30:00', 'DIET_CONSULTATION',   'BOOKED',    NULL,                                90.00::NUMERIC),
  ('g.marino@gmail.com',    's.scironi@apiceclinic.it', TIMESTAMP '2026-06-04 08:30:00', 'SPORT_MEDICAL_VISIT', 'BOOKED',    'Idoneita competizione regionale',  100.00::NUMERIC)
) AS appt(patient_email, specialist_email, scheduled_at, service_type, status, notes, price)
JOIN patient    p ON p.email = appt.patient_email
JOIN specialist s ON s.email = appt.specialist_email;
