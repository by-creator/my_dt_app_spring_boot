CREATE TABLE IF NOT EXISTS gfa_wifi_settings (
                                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                 ssid VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO gfa_wifi_settings (id, ssid, password, updated_at)
VALUES (1, 'DakarTerminal_WiFi', '', NOW())
    ON DUPLICATE KEY UPDATE id = id;