-- V106_1: Seed services for each specialist, area_id derived from specialist

-- ── Area 1: Alimentazione ── Simona Ruberti (NUTRITIONIST) ─────────────────
INSERT INTO services (service, price, specialist_id, area_id)
SELECT 'Visita nutrizionale iniziale',           90.00,  s.id, s.area_id FROM specialist s WHERE s.email = 's.ruberti@apiceclinic.it';
INSERT INTO services (service, price, specialist_id, area_id)
SELECT 'Piano alimentare personalizzato',        120.00, s.id, s.area_id FROM specialist s WHERE s.email = 's.ruberti@apiceclinic.it';
INSERT INTO services (service, price, specialist_id, area_id)
SELECT 'Consulenza nutrizionale di follow-up',   60.00,  s.id, s.area_id FROM specialist s WHERE s.email = 's.ruberti@apiceclinic.it';

-- ── Area 1: Alimentazione ── Cristiana Maratti (DIETOLOGIST) ────────────────
INSERT INTO services (service, price, specialist_id, area_id)
SELECT 'Prima visita dietologica',                        110.00, s.id, s.area_id FROM specialist s WHERE s.email = 'c.maratti@apiceclinic.it';
INSERT INTO services (service, price, specialist_id, area_id)
SELECT 'Piano dietetico per disturbi metabolici',         150.00, s.id, s.area_id FROM specialist s WHERE s.email = 'c.maratti@apiceclinic.it';
INSERT INTO services (service, price, specialist_id, area_id)
SELECT 'Controllo e aggiornamento piano dietetico',       70.00,  s.id, s.area_id FROM specialist s WHERE s.email = 'c.maratti@apiceclinic.it';

-- ── Area 2: Sport ── Luca Siretta (PERSONAL_TRAINER) ────────────────────────
INSERT INTO services (service, price, specialist_id, area_id)
SELECT 'Seduta di personal training (60 min)',            50.00,  s.id, s.area_id FROM specialist s WHERE s.email = 'l.siretta@apiceclinic.it';
INSERT INTO services (service, price, specialist_id, area_id)
SELECT 'Programma di allenamento personalizzato',         80.00,  s.id, s.area_id FROM specialist s WHERE s.email = 'l.siretta@apiceclinic.it';
INSERT INTO services (service, price, specialist_id, area_id)
SELECT 'Valutazione posturale e funzionale',              70.00,  s.id, s.area_id FROM specialist s WHERE s.email = 'l.siretta@apiceclinic.it';
INSERT INTO services (service, price, specialist_id, area_id)
SELECT 'Pacchetto 10 sedute personal training',           450.00, s.id, s.area_id FROM specialist s WHERE s.email = 'l.siretta@apiceclinic.it';

-- ── Area 3: Clinica ── Sandro Scironi (SPORT_DOCTOR) ────────────────────────
INSERT INTO services (service, price, specialist_id, area_id)
SELECT 'Visita medico-sportiva',                          120.00, s.id, s.area_id FROM specialist s WHERE s.email = 's.scironi@apiceclinic.it';
INSERT INTO services (service, price, specialist_id, area_id)
SELECT 'Certificato idoneità sportiva agonistica',        150.00, s.id, s.area_id FROM specialist s WHERE s.email = 's.scironi@apiceclinic.it';
INSERT INTO services (service, price, specialist_id, area_id)
SELECT 'Elettrocardiogramma sotto sforzo',                200.00, s.id, s.area_id FROM specialist s WHERE s.email = 's.scironi@apiceclinic.it';

-- ── Area 3: Clinica ── Michele Lavori (PHYSIOTHERAPIST) ─────────────────────
INSERT INTO services (service, price, specialist_id, area_id)
SELECT 'Valutazione fisioterapica',                       80.00,  s.id, s.area_id FROM specialist s WHERE s.email = 'm.lavori@apiceclinic.it';
INSERT INTO services (service, price, specialist_id, area_id)
SELECT 'Seduta di fisioterapia (45 min)',                  55.00,  s.id, s.area_id FROM specialist s WHERE s.email = 'm.lavori@apiceclinic.it';
INSERT INTO services (service, price, specialist_id, area_id)
SELECT 'Trattamento manuale ortopedico',                  65.00,  s.id, s.area_id FROM specialist s WHERE s.email = 'm.lavori@apiceclinic.it';
INSERT INTO services (service, price, specialist_id, area_id)
SELECT 'Ciclo riabilitativo post-operatorio (10 sedute)', 500.00, s.id, s.area_id FROM specialist s WHERE s.email = 'm.lavori@apiceclinic.it';
