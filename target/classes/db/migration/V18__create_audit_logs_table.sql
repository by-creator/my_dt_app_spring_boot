CREATE TABLE IF NOT EXISTS audit_logs (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_email  VARCHAR(150),
    user_name   VARCHAR(100),
    action      VARCHAR(50)  NOT NULL,
    entity_type VARCHAR(100),
    entity_id   BIGINT,
    details     TEXT,
    ip_address  VARCHAR(50),
    user_agent  VARCHAR(500),
    status      VARCHAR(20)  NOT NULL DEFAULT 'SUCCESS',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_audit_user_email  (user_email),
    INDEX idx_audit_action      (action),
    INDEX idx_audit_entity_type (entity_type),
    INDEX idx_audit_created_at  (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
