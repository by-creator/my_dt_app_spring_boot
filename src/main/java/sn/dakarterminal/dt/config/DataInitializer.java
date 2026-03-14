package sn.dakarterminal.dt.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sn.dakarterminal.dt.entity.Role;
import sn.dakarterminal.dt.entity.User;
import sn.dakarterminal.dt.repository.RoleRepository;
import sn.dakarterminal.dt.repository.UserRepository;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Initialise les données de base au démarrage de l'application :
 *  - Les rôles système s'ils n'existent pas
 *  - Un compte administrateur par défaut s'il n'existe pas
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.init.admin-email}")
    private String adminEmail;

    @Value("${app.init.admin-password}")
    private String adminPassword;

    @Value("${app.init.admin-name}")
    private String adminName;

    /** Rôles système à créer s'ils sont absents (nom -> description) */
    private static final Map<String, String> SYSTEM_ROLES = new LinkedHashMap<>();
    static {
        SYSTEM_ROLES.put("ADMIN",               "Administrateur système avec accès complet");
        SYSTEM_ROLES.put("SUPER_U",             "Super utilisateur avec droits étendus");
        SYSTEM_ROLES.put("FACTURATION",         "Responsable facturation");
        SYSTEM_ROLES.put("CLIENT_FACTURATION",  "Client accès facturation");
        SYSTEM_ROLES.put("OPERATIONS",          "Responsable opérations portuaires");
        SYSTEM_ROLES.put("PLANIFICATION",       "Responsable planification");
        SYSTEM_ROLES.put("INFORMATIQUE",        "Équipe informatique");
        SYSTEM_ROLES.put("DOUANE",              "Agent des douanes");
        SYSTEM_ROLES.put("GFA",                 "Gestionnaire GFA");
        SYSTEM_ROLES.put("IPAKI",               "Opérateur IPAKI");
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        initRoles();
        initAdminUser();
    }

    private void initRoles() {
        SYSTEM_ROLES.forEach((name, description) -> {
            if (!roleRepository.existsByName(name)) {
                roleRepository.save(Role.builder()
                        .name(name)
                        .description(description)
                        .actif(true)
                        .build());
                log.info("Rôle créé au démarrage : {}", name);
            }
        });
    }

    private void initAdminUser() {
        if (userRepository.existsByEmail(adminEmail)) {
            log.debug("Compte admin déjà existant : {}", adminEmail);
            return;
        }

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new IllegalStateException("Rôle ADMIN introuvable après initialisation"));

        User admin = User.builder()
                .name(adminName)
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .role(adminRole)
                .twoFactorEnabled(false)
                .actif(true)
                .build();

        userRepository.save(admin);
        log.warn("========================================================");
        log.warn("  Compte administrateur créé au premier démarrage :");
        log.warn("  Email    : {}", adminEmail);
        log.warn("  Password : {} (modifiez-le immédiatement !)", adminPassword);
        log.warn("========================================================");
    }
}
