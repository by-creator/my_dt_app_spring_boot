package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.PreparedStatement;

/**
 * Resets the admin user password to the default value.
 * Required when the stored BCrypt hash doesn't match the configured default password.
 */
public class V19__ResetAdminPassword extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        String hashedPassword = encoder.encode("Admin@2024!");

        try (PreparedStatement stmt = context.getConnection().prepareStatement(
                "UPDATE users SET password = ? WHERE email = 'admin@dakarterminal.sn'")) {
            stmt.setString(1, hashedPassword);
            stmt.executeUpdate();
        }
    }
}
