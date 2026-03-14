CREATE TABLE IF NOT EXISTS services (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    code VARCHAR(50) UNIQUE,
    actif TINYINT(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS guichets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero VARCHAR(20) NOT NULL,
    service_id BIGINT,
    actif TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT fk_guichet_service FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS agents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100),
    service_id BIGINT,
    guichet_id BIGINT,
    actif TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT fk_agent_service FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE SET NULL,
    CONSTRAINT fk_agent_guichet FOREIGN KEY (guichet_id) REFERENCES guichets(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tickets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_id BIGINT,
    agent_id BIGINT,
    guichet_id BIGINT,
    statut VARCHAR(50) NOT NULL DEFAULT 'EN_ATTENTE',
    numero VARCHAR(20) NOT NULL,
    nom_client VARCHAR(150),
    motif TEXT,
    called_at DATETIME,
    closed_at DATETIME,
    processing_time BIGINT,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT fk_ticket_service FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE SET NULL,
    CONSTRAINT fk_ticket_agent FOREIGN KEY (agent_id) REFERENCES agents(id) ON DELETE SET NULL,
    CONSTRAINT fk_ticket_guichet FOREIGN KEY (guichet_id) REFERENCES guichets(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_ticket_statut ON tickets(statut);
CREATE INDEX idx_ticket_service ON tickets(service_id);
CREATE INDEX idx_ticket_created ON tickets(created_at);
