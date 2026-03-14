CREATE TABLE IF NOT EXISTS dossier_facturation_proformas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dossier_id BIGINT NOT NULL,
    file_path TEXT,
    generated_at DATETIME,
    validated_at DATETIME,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT fk_proforma_dossier FOREIGN KEY (dossier_id) REFERENCES dossier_facturations(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS dossier_facturation_factures (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dossier_id BIGINT NOT NULL,
    file_path TEXT,
    numero_facture VARCHAR(50),
    validated_at DATETIME,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT fk_facture_dossier FOREIGN KEY (dossier_id) REFERENCES dossier_facturations(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS dossier_facturation_bons (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dossier_id BIGINT NOT NULL,
    file_path TEXT,
    numero_bon VARCHAR(50),
    validated_at DATETIME,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT fk_bon_dossier FOREIGN KEY (dossier_id) REFERENCES dossier_facturations(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
