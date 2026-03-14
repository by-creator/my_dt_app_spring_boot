package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

/**
 * Resets the admin user password to the default value.
 * Safe to run on fresh databases — skips if the users table doesn't exist yet.
 */
public class V10__ResetAdminPassword extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        // Skip silently on fresh databases where users table doesn't exist yet
        DatabaseMetaData meta = context.getConnection().getMetaData();
        String catalog = context.getConnection().getCatalog();
        try (ResultSet rs = meta.getTables(catalog, null, "users", new String[]{"TABLE"})) {
            if (!rs.next()) {
                return; // table not yet created — nothing to do
            }
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        String hashedPassword = encoder.encode("Admin@2024!");

        try (PreparedStatement stmt = context.getConnection().prepareStatement(
                "UPDATE users SET password = ? WHERE email = 'admin@dakarterminal.sn'")) {
            stmt.setString(1, hashedPassword);
            stmt.executeUpdate();
        }
    }
}
