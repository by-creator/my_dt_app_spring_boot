CREATE TABLE IF NOT EXISTS tiers_ipaki (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code_tiers VARCHAR(50) UNIQUE,
    nom VARCHAR(200),
    adresse TEXT,
    telephone VARCHAR(50),
    email VARCHAR(150),
    ninea VARCHAR(50),
    rc VARCHAR(50),
    type_tiers VARCHAR(50),
    actif TINYINT(1) NOT NULL DEFAULT 1,
    created_at DATETIME,
    updated_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_tiers_code ON tiers_ipaki(code_tiers);
CREATE INDEX idx_tiers_nom ON tiers_ipaki(nom);
