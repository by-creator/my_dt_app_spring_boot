CREATE TABLE IF NOT EXISTS employee_temporaire_demandes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100),
    fonction VARCHAR(100),
    service VARCHAR(100),
    date_debut DATE,
    date_fin DATE,
    motif TEXT,
    statut VARCHAR(50) DEFAULT 'EN_ATTENTE',
    numero_badge VARCHAR(50),
    validated_by BIGINT,
    validated_at DATETIME,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT fk_emp_temp_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_emp_temp_statut ON employee_temporaire_demandes(statut);
CREATE INDEX idx_emp_temp_user ON employee_temporaire_demandes(user_id);
