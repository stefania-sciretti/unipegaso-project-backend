-- V105: Create services table
CREATE TABLE services (
    id            BIGSERIAL     PRIMARY KEY,
    service       VARCHAR(255)  NOT NULL,
    price         NUMERIC(10,2) NOT NULL,
    specialist_id BIGINT        NOT NULL REFERENCES specialist(id)
);

CREATE INDEX idx_services_specialist_id ON services(specialist_id);
