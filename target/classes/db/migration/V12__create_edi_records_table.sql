CREATE TABLE IF NOT EXISTS edi_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    message_type VARCHAR(50),
    sender VARCHAR(100),
    receiver VARCHAR(100),
    reference VARCHAR(100),
    file_name VARCHAR(255),
    file_path TEXT,
    content LONGTEXT,
    statut VARCHAR(50) DEFAULT 'RECU',
    processed TINYINT(1) NOT NULL DEFAULT 0,
    error_message TEXT,
    processed_at DATETIME,
    created_at DATETIME,
    updated_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_edi_type ON edi_records(message_type);
CREATE INDEX idx_edi_processed ON edi_records(processed);
CREATE INDEX idx_edi_statut ON edi_records(statut);
