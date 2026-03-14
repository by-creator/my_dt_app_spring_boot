CREATE TABLE IF NOT EXISTS ordre_approches (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_oa VARCHAR(100) UNIQUE,
    navire VARCHAR(100),
    armateur VARCHAR(150),
    consignataire VARCHAR(150),
    terminal VARCHAR(100),
    quai VARCHAR(100),
    voyage VARCHAR(50),
    date_arrivee_prevue DATE,
    date_depart_prevue DATE,
    date_arrivee_reelle DATE,
    date_depart_reel DATE,
    statut VARCHAR(50) DEFAULT 'PLANIFIE',
    pavillon VARCHAR(50),
    jauge VARCHAR(50),
    tirant_eau VARCHAR(50),
    longueur VARCHAR(50),
    notes TEXT,
    created_at DATETIME,
    updated_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_oa_statut ON ordre_approches(statut);
CREATE INDEX idx_oa_date ON ordre_approches(date_arrivee_prevue);

CREATE TABLE IF NOT EXISTS ordre_approche_stagings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_oa VARCHAR(100),
    navire VARCHAR(100),
    armateur VARCHAR(150),
    consignataire VARCHAR(150),
    terminal VARCHAR(100),
    quai VARCHAR(100),
    voyage VARCHAR(50),
    date_arrivee_prevue DATE,
    date_depart_prevue DATE,
    statut VARCHAR(50),
    batch_id VARCHAR(100),
    import_errors TEXT,
    processed TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME,
    updated_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
