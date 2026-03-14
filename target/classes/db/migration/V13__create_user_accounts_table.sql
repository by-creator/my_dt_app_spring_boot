CREATE TABLE IF NOT EXISTS user_accounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNIQUE,
    numero_compte VARCHAR(50) UNIQUE,
    solde DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    credit_limite DECIMAL(15,2),
    devise VARCHAR(10) DEFAULT 'XOF',
    actif TINYINT(1) NOT NULL DEFAULT 1,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT fk_account_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS scan_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    user_id BIGINT,
    entity_type VARCHAR(50),
    entity_id BIGINT,
    expires_at DATETIME,
    used_at DATETIME,
    used TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME,
    CONSTRAINT fk_scan_token_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_scan_token ON scan_tokens(token);
