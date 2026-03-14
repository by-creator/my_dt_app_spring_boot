CREATE TABLE IF NOT EXISTS yards (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_conteneur VARCHAR(20),
    type_conteneur VARCHAR(20),
    etat VARCHAR(20),
    position VARCHAR(50),
    navire VARCHAR(100),
    armateur VARCHAR(150),
    client VARCHAR(150),
    bl VARCHAR(100),
    date_arrivee DATE,
    date_depart DATE,
    date_sortie DATE,
    statut VARCHAR(50) DEFAULT 'EN_PARC',
    poids DOUBLE,
    iso_code VARCHAR(10),
    notes TEXT,
    created_at DATETIME,
    updated_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_yard_conteneur ON yards(numero_conteneur);
CREATE INDEX idx_yard_statut ON yards(statut);
CREATE INDEX idx_yard_navire ON yards(navire);

CREATE TABLE IF NOT EXISTS yard_stagings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_conteneur VARCHAR(20),
    type_conteneur VARCHAR(20),
    etat VARCHAR(20),
    position VARCHAR(50),
    navire VARCHAR(100),
    armateur VARCHAR(150),
    client VARCHAR(150),
    bl VARCHAR(100),
    date_arrivee DATE,
    date_depart DATE,
    statut VARCHAR(50),
    poids DOUBLE,
    iso_code VARCHAR(10),
    batch_id VARCHAR(100),
    import_errors TEXT,
    processed TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME,
    updated_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
