-- V109 H2: Seed patients and specialists
-- Plain INSERT (no ON CONFLICT needed — H2 in-memory resets on every test run)

INSERT INTO patient (first_name, last_name, fiscal_code, birth_date, email, phone)
VALUES
('Alessandro', 'Russo',    'RSSALS80A01H501R', '1980-01-01', 'a.russo@gmail.com',      '+39 333 1001001'),
('Roberto',    'Esposito', 'SPTRRT75T01L219V', '1975-12-01', 'r.esposito@libero.it',   '+39 328 3003003'),
('Davide',     'Romano',   'RMNDVD88P01G273X', '1988-09-01', 'd.romano@yahoo.it',      NULL),
('Marco',      'Colombo',  'CLMMRC83C15H501A', '1983-03-15', 'm.colombo@gmail.com',    '+39 333 5005005'),
('Luca',       'Ricci',    'RCCLCU90L22F205B', '1990-07-22', 'l.ricci@libero.it',      NULL),
('Giovanni',   'Marino',   'MRNGNN72S08L219C', '1972-11-08', 'g.marino@gmail.com',     '+39 347 7007007'),
('Matteo',     'Bruno',    'BRNMTT95B28F205E', '1995-02-28', 'm.bruno@gmail.com',      '+39 333 9009009'),
('Lorenzo',    'Gallo',    'GLLLRN87M14L219F', '1987-08-14', 'l.gallo@outlook.com',    NULL),
('Stefano',    'Conti',    'CNTSFN78D03H501G', '1978-04-03', 's.conti@libero.it',      '+39 347 1101011'),
('Sara',       'Colombo',  'CLMSRA91S65H501U', '1991-11-25', 's.colombo@gmail.com',    '+39 347 5301053'),
('Elena',      'Ricci',    'RCCLNE87C48L219V', '1987-03-08', 'e.ricci@yahoo.it',       NULL);

INSERT INTO specialist (first_name, last_name, role, bio, email)
VALUES
('Simona',    'Ruberti', 'NUTRITIONIST',    'Nutrizionista sportiva con esperienza in piani alimentari per atleti e sportivi amatoriali.', 's.ruberti@apiceclinic.it'),
('Luca',      'Siretta', 'PERSONAL_TRAINER','Personal trainer certificato NSCA. Specializzato in allenamento funzionale e riabilitazione posturale.', 'l.siretta@apiceclinic.it'),
('Sandro',    'Scironi', 'SPORT_DOCTOR',    'Medico dello Sport con specializzazione in medicina preventiva e idoneita sportiva.', 's.scironi@apiceclinic.it'),
('Cristiana', 'Maratti', 'DIETOLOGIST',     'Dietologa con esperienza in nutrizione clinica, disturbi metabolici e piani dietetici personalizzati.', 'c.maratti@apiceclinic.it'),
('Michele',   'Lavori',  'PHYSIOTHERAPIST', 'Fisioterapista specializzato in fisioterapia sportiva, ortopedica e riabilitazione post-operatoria.', 'm.lavori@apiceclinic.it');

-- Link area_id: V106 ran before specialists were seeded, so we re-apply the same rules here
UPDATE specialist SET area_id = 1 WHERE role IN ('NUTRITIONIST', 'DIETOLOGIST');
UPDATE specialist SET area_id = 2 WHERE role = 'PERSONAL_TRAINER';
UPDATE specialist SET area_id = 3 WHERE role IN ('SPORT_DOCTOR', 'PHYSIOTHERAPIST');
