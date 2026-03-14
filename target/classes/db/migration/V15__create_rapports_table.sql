CREATE TABLE IF NOT EXISTS rapport_infos_facturations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    periode_debut DATE,
    periode_fin DATE,
    total_factures INT,
    total_montant_ht DECIMAL(15,2),
    total_montant_tva DECIMAL(15,2),
    total_montant_ttc DECIMAL(15,2),
    generated_by BIGINT,
    file_path TEXT,
    created_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS rapport_infos_yards (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    periode_debut DATE,
    periode_fin DATE,
    total_conteneurs INT,
    total_plein INT,
    total_vide INT,
    generated_by BIGINT,
    file_path TEXT,
    created_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS rapport_suivi_detail_facturations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_bl VARCHAR(100),
    client VARCHAR(150),
    navire VARCHAR(100),
    date_proforma DATE,
    date_facture DATE,
    date_bon DATE,
    montant_ttc DECIMAL(15,2),
    statut_dossier VARCHAR(50),
    delai_proforma BIGINT,
    delai_facture BIGINT,
    delai_bon BIGINT,
    rapport_date DATE,
    generated_by BIGINT,
    created_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
