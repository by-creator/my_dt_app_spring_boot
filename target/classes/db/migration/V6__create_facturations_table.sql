CREATE TABLE IF NOT EXISTS facturations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_bl VARCHAR(100),
    numero_facture VARCHAR(100),
    numero_proforma VARCHAR(100),
    navire VARCHAR(100),
    armateur VARCHAR(150),
    consignataire VARCHAR(150),
    client VARCHAR(150),
    code_client VARCHAR(50),
    nombre_conteneurs INT,
    type_conteneur VARCHAR(50),
    poids DECIMAL(15,3),
    volume DECIMAL(15,3),
    montant_ht DECIMAL(15,2),
    montant_tva DECIMAL(15,2),
    montant_ttc DECIMAL(15,2),
    date_arrivee DATE,
    date_depart DATE,
    date_proforma DATE,
    date_facture DATE,
    statut VARCHAR(50),
    terminal VARCHAR(100),
    voyage VARCHAR(50),
    origine VARCHAR(100),
    destination VARCHAR(100),
    regime VARCHAR(50),
    notes TEXT,
    created_at DATETIME,
    updated_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_facturation_bl ON facturations(numero_bl);
CREATE INDEX idx_facturation_client ON facturations(client);
CREATE INDEX idx_facturation_date ON facturations(date_facture);

CREATE TABLE IF NOT EXISTS facturation_stagings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_bl VARCHAR(100),
    numero_facture VARCHAR(100),
    numero_proforma VARCHAR(100),
    navire VARCHAR(100),
    armateur VARCHAR(150),
    consignataire VARCHAR(150),
    client VARCHAR(150),
    code_client VARCHAR(50),
    nombre_conteneurs INT,
    type_conteneur VARCHAR(50),
    poids DECIMAL(15,3),
    volume DECIMAL(15,3),
    montant_ht DECIMAL(15,2),
    montant_tva DECIMAL(15,2),
    montant_ttc DECIMAL(15,2),
    date_arrivee DATE,
    date_depart DATE,
    date_proforma DATE,
    date_facture DATE,
    statut VARCHAR(50),
    terminal VARCHAR(100),
    voyage VARCHAR(50),
    origine VARCHAR(100),
    destination VARCHAR(100),
    regime VARCHAR(50),
    batch_id VARCHAR(100),
    import_errors TEXT,
    processed TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME,
    updated_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_fact_staging_batch ON facturation_stagings(batch_id);
CREATE INDEX idx_fact_staging_processed ON facturation_stagings(processed);
