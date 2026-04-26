-- ===================================================
-- Users table for JWT authentication (H2-compatible)
-- ===================================================

CREATE TABLE IF NOT EXISTS users (
    id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email    VARCHAR(255),
    role     VARCHAR(50)  NOT NULL DEFAULT 'ROLE_USER',
    enabled  BOOLEAN      NOT NULL DEFAULT TRUE
);

-- Gli utenti di default verranno creati dal DataInitializer al avvio
-- (admin/admin123 e user/user123)

