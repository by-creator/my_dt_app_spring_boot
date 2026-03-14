CREATE TABLE IF NOT EXISTS rattachement_bls (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    nom VARCHAR(100),
    prenom VARCHAR(100),
    email VARCHAR(150),
    bl VARCHAR(100) NOT NULL,
    compte VARCHAR(100),
    statut VARCHAR(50) NOT NULL DEFAULT 'EN_ATTENTE',
    time_elapsed BIGINT,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT fk_rattachement_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_rattachement_bl ON rattachement_bls(bl);
CREATE INDEX idx_rattachement_user ON rattachement_bls(user_id);
CREATE INDEX idx_rattachement_statut ON rattachement_bls(statut);
