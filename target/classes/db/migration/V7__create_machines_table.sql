CREATE TABLE IF NOT EXISTS machines (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_serie VARCHAR(100) UNIQUE,
    modele VARCHAR(100),
    type VARCHAR(50),
    utilisateur VARCHAR(150),
    service VARCHAR(100),
    site VARCHAR(100),
    marque VARCHAR(100),
    date_acquisition DATE,
    date_garantie DATE,
    etat VARCHAR(50) DEFAULT 'EN_SERVICE',
    notes TEXT,
    created_at DATETIME,
    updated_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_machine_type ON machines(type);
CREATE INDEX idx_machine_service ON machines(service);
CREATE INDEX idx_machine_utilisateur ON machines(utilisateur);
