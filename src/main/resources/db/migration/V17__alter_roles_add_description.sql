-- Alter roles table: change name from enum to VARCHAR and add description/actif/timestamps
ALTER TABLE roles
    MODIFY COLUMN name VARCHAR(50) NOT NULL,
    ADD COLUMN description VARCHAR(255) NULL AFTER name,
    ADD COLUMN actif TINYINT(1) NOT NULL DEFAULT 1 AFTER description,
    ADD COLUMN created_at DATETIME NULL AFTER actif,
    ADD COLUMN updated_at DATETIME NULL AFTER created_at;

-- Update descriptions for existing roles
UPDATE roles SET description = 'Administrateur système avec accès complet' WHERE name = 'ADMIN';
UPDATE roles SET description = 'Super utilisateur avec droits étendus' WHERE name = 'SUPER_U';
UPDATE roles SET description = 'Responsable facturation' WHERE name = 'FACTURATION';
UPDATE roles SET description = 'Client accès facturation' WHERE name = 'CLIENT_FACTURATION';
UPDATE roles SET description = 'Responsable opérations portuaires' WHERE name = 'OPERATIONS';
UPDATE roles SET description = 'Responsable planification' WHERE name = 'PLANIFICATION';
UPDATE roles SET description = 'Équipe informatique' WHERE name = 'INFORMATIQUE';
UPDATE roles SET description = 'Agent des douanes' WHERE name = 'DOUANE';
UPDATE roles SET description = 'Gestionnaire GFA' WHERE name = 'GFA';
UPDATE roles SET description = 'Opérateur IPAKI' WHERE name = 'IPAKI';

-- Set timestamps for existing roles
UPDATE roles SET created_at = NOW(), updated_at = NOW() WHERE created_at IS NULL;
