CREATE TABLE IF NOT EXISTS dossier_facturations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    rattachement_bl_id BIGINT,
    statut VARCHAR(50) NOT NULL DEFAULT 'EN_ATTENTE_VALIDATION',
    date_proforma DATETIME,
    time_elapsed_proforma BIGINT,
    time_elapsed_facture BIGINT,
    time_elapsed_bon BIGINT,
    relance_proforma INT NOT NULL DEFAULT 0,
    relance_facture INT NOT NULL DEFAULT 0,
    relance_bon INT NOT NULL DEFAULT 0,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT fk_dossier_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT fk_dossier_rattachement FOREIGN KEY (rattachement_bl_id) REFERENCES rattachement_bls(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_dossier_statut ON dossier_facturations(statut);
CREATE INDEX idx_dossier_user ON dossier_facturations(user_id);
