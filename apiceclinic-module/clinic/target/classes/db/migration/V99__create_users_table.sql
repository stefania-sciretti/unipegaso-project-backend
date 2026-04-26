-- Tabella per la gestione degli utenti e autenticazione JWT
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    role VARCHAR(50) NOT NULL DEFAULT 'ROLE_USER',
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Indice per velocizzare le ricerche per username
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);

-- Gli utenti di default verranno creati dal DataInitializer al avvio
-- (admin/admin123 e user/user123)

