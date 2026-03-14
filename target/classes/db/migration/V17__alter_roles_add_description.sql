-- Alter roles table: change name from enum to VARCHAR and add description/actif/timestamps
-- This migration is idempotent: safe to re-run if partially applied

-- MODIFY name to VARCHAR (no-op if already VARCHAR)
ALTER TABLE roles MODIFY COLUMN name VARCHAR(50) NOT NULL;

-- Add description column if not exists
SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'roles' AND COLUMN_NAME = 'description');
SET @sql = IF(@col_exists = 0,
    'ALTER TABLE roles ADD COLUMN description VARCHAR(255) NULL AFTER name',
    'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Add actif column if not exists
SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'roles' AND COLUMN_NAME = 'actif');
SET @sql = IF(@col_exists = 0,
    'ALTER TABLE roles ADD COLUMN actif TINYINT(1) NOT NULL DEFAULT 1',
    'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Add created_at column if not exists
SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'roles' AND COLUMN_NAME = 'created_at');
SET @sql = IF(@col_exists = 0,
    'ALTER TABLE roles ADD COLUMN created_at DATETIME NULL',
    'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Add updated_at column if not exists
SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'roles' AND COLUMN_NAME = 'updated_at');
SET @sql = IF(@col_exists = 0,
    'ALTER TABLE roles ADD COLUMN updated_at DATETIME NULL',
    'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Update descriptions for existing roles (idempotent)
UPDATE roles SET description = 'Administrateur systeme avec acces complet' WHERE name = 'ADMIN' AND (description IS NULL OR description = '');
UPDATE roles SET description = 'Super utilisateur avec droits etendus' WHERE name = 'SUPER_U' AND (description IS NULL OR description = '');
UPDATE roles SET description = 'Responsable facturation' WHERE name = 'FACTURATION' AND (description IS NULL OR description = '');
UPDATE roles SET description = 'Client acces facturation' WHERE name = 'CLIENT_FACTURATION' AND (description IS NULL OR description = '');
UPDATE roles SET description = 'Responsable operations portuaires' WHERE name = 'OPERATIONS' AND (description IS NULL OR description = '');
UPDATE roles SET description = 'Responsable planification' WHERE name = 'PLANIFICATION' AND (description IS NULL OR description = '');
UPDATE roles SET description = 'Equipe informatique' WHERE name = 'INFORMATIQUE' AND (description IS NULL OR description = '');
UPDATE roles SET description = 'Agent des douanes' WHERE name = 'DOUANE' AND (description IS NULL OR description = '');
UPDATE roles SET description = 'Gestionnaire GFA' WHERE name = 'GFA' AND (description IS NULL OR description = '');
UPDATE roles SET description = 'Operateur IPAKI' WHERE name = 'IPAKI' AND (description IS NULL OR description = '');

-- Set timestamps for existing roles
UPDATE roles SET created_at = NOW(), updated_at = NOW() WHERE created_at IS NULL;
