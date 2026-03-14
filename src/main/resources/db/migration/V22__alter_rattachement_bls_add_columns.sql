-- Add maison_transit and motif_rejet to rattachement_bls (idempotent)

SET @col1 = (SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'rattachement_bls' AND COLUMN_NAME = 'maison_transit');
SET @sql1 = IF(@col1 = 0,
    'ALTER TABLE rattachement_bls ADD COLUMN maison_transit VARCHAR(150) NULL AFTER email',
    'SELECT 1');
PREPARE stmt1 FROM @sql1; EXECUTE stmt1; DEALLOCATE PREPARE stmt1;

SET @col2 = (SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'rattachement_bls' AND COLUMN_NAME = 'motif_rejet');
SET @sql2 = IF(@col2 = 0,
    'ALTER TABLE rattachement_bls ADD COLUMN motif_rejet TEXT NULL',
    'SELECT 1');
PREPARE stmt2 FROM @sql2; EXECUTE stmt2; DEALLOCATE PREPARE stmt2;
