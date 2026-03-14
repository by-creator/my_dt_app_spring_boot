INSERT IGNORE INTO roles (name) VALUES
    ('ADMIN'),
    ('SUPER_U'),
    ('FACTURATION'),
    ('CLIENT_FACTURATION'),
    ('OPERATIONS'),
    ('PLANIFICATION'),
    ('INFORMATIQUE'),
    ('DOUANE'),
    ('GFA'),
    ('IPAKI');

-- Initial admin user (password: Admin@2024! - BCrypt hash)
INSERT IGNORE INTO users (name, email, password, role_id, two_factor_enabled, actif, created_at, updated_at)
SELECT 'Administrateur', 'admin@dakarterminal.sn',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj3oQEHCLe2.',
    r.id, 0, 1, NOW(), NOW()
FROM roles r WHERE r.name = 'ADMIN'
LIMIT 1;
