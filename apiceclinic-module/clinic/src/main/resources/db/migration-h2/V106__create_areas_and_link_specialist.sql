-- V106 H2: Create areas table, seed data, link specialists, and add area_id to services

CREATE TABLE areas (
    id   BIGINT       PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

INSERT INTO areas (id, name) VALUES
    (1, 'Alimentazione'),
    (2, 'Sport'),
    (3, 'Clinica');

ALTER TABLE specialist
    ADD COLUMN area_id BIGINT REFERENCES areas(id);

UPDATE specialist SET area_id = 1 WHERE role IN ('NUTRITIONIST', 'DIETOLOGIST');
UPDATE specialist SET area_id = 2 WHERE role = 'PERSONAL_TRAINER';
UPDATE specialist SET area_id = 3 WHERE role IN ('SPORT_DOCTOR', 'PHYSIOTHERAPIST');

CREATE INDEX idx_specialist_area_id ON specialist(area_id);

ALTER TABLE services
    ADD COLUMN area_id BIGINT REFERENCES areas(id);

UPDATE services s
SET area_id = (SELECT sp.area_id FROM specialist sp WHERE sp.id = s.specialist_id);

CREATE INDEX idx_services_area_id ON services(area_id);
