package sn.dakarterminal.dt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.dakarterminal.dt.dto.RoleDto;
import sn.dakarterminal.dt.dto.UserCreateDto;
import sn.dakarterminal.dt.dto.UserDto;
import sn.dakarterminal.dt.entity.Role;
import sn.dakarterminal.dt.entity.User;
import sn.dakarterminal.dt.exception.ResourceNotFoundException;
import sn.dakarterminal.dt.repository.RoleRepository;
import sn.dakarterminal.dt.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditService auditService;

    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userRepository.findAllActiveWithRole().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec l'id: " + id));
        return toDto(user);
    }

    @Transactional(readOnly = true)
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec l'email: " + email));
        return toDto(user);
    }

    public UserDto create(UserCreateDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email déjà utilisé: " + dto.getEmail());
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new IllegalArgumentException("Le mot de passe est obligatoire pour la création");
        }

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .twoFactorEnabled(dto.getTwoFactorEnabled() != null ? dto.getTwoFactorEnabled() : false)
                .actif(true)
                .build();

        if (dto.getRoleId() != null) {
            Role role = roleRepository.findById(dto.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Rôle introuvable avec l'id: " + dto.getRoleId()));
            user.setRole(role);
        }

        User saved = userRepository.save(user);
        auditService.logCreate("USER", saved.getId(),
                "Création de l'utilisateur: " + saved.getName() + " (" + saved.getEmail() + ")");
        log.info("Utilisateur créé: {} (id={})", saved.getEmail(), saved.getId());
        return toDto(saved);
    }

    public UserDto update(Long id, UserCreateDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec l'id: " + id));

        String oldEmail = user.getEmail();
        user.setName(dto.getName());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getTwoFactorEnabled() != null) {
            user.setTwoFactorEnabled(dto.getTwoFactorEnabled());
        }
        if (dto.getRoleId() != null) {
            Role role = roleRepository.findById(dto.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Rôle introuvable avec l'id: " + dto.getRoleId()));
            user.setRole(role);
        }

        User saved = userRepository.save(user);
        auditService.logUpdate("USER", id,
                "Mise à jour de l'utilisateur: " + oldEmail + " -> " + saved.getName());
        log.info("Utilisateur mis à jour: {} (id={})", saved.getEmail(), saved.getId());
        return toDto(saved);
    }

    public void deactivate(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec l'id: " + id));
        user.setActif(false);
        userRepository.save(user);
        auditService.log("DEACTIVATE", "USER", id,
                "Désactivation de l'utilisateur: " + user.getEmail());
        log.info("Utilisateur désactivé: {} (id={})", user.getEmail(), id);
    }

    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec l'id: " + id));
        String email = user.getEmail();
        userRepository.deleteById(id);
        auditService.logDelete("USER", id, "Suppression de l'utilisateur: " + email);
        log.info("Utilisateur supprimé: {} (id={})", email, id);
    }

    public UserDto toDto(User user) {
        RoleDto roleDto = null;
        if (user.getRole() != null) {
            roleDto = RoleDto.builder()
                    .id(user.getRole().getId())
                    .name(user.getRole().getName())
                    .description(user.getRole().getDescription())
                    .actif(user.getRole().getActif())
                    .build();
        }
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(roleDto)
                .twoFactorEnabled(user.getTwoFactorEnabled())
                .emailVerifiedAt(user.getEmailVerifiedAt())
                .actif(user.getActif())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
