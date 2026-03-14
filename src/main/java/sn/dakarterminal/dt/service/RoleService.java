package sn.dakarterminal.dt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.dakarterminal.dt.dto.RoleCreateDto;
import sn.dakarterminal.dt.dto.RoleDto;
import sn.dakarterminal.dt.entity.Role;
import sn.dakarterminal.dt.exception.ResourceNotFoundException;
import sn.dakarterminal.dt.repository.RoleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {

    private final RoleRepository roleRepository;
    private final AuditService auditService;

    public List<RoleDto> findAll() {
        return roleRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<RoleDto> findAllActive() {
        return roleRepository.findByActifTrue().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public RoleDto findById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rôle introuvable avec l'id: " + id));
        return toDto(role);
    }

    @Transactional
    public RoleDto create(RoleCreateDto dto) {
        String normalizedName = dto.getName().toUpperCase().trim();
        if (roleRepository.existsByName(normalizedName)) {
            throw new IllegalArgumentException("Un rôle avec le nom '" + normalizedName + "' existe déjà");
        }

        Role role = Role.builder()
                .name(normalizedName)
                .description(dto.getDescription())
                .actif(dto.getActif() != null ? dto.getActif() : true)
                .build();

        Role saved = roleRepository.save(role);
        auditService.logCreate("ROLE", saved.getId(), "Création du rôle: " + saved.getName());
        log.info("Rôle créé: {} (id={})", saved.getName(), saved.getId());
        return toDto(saved);
    }

    @Transactional
    public RoleDto update(Long id, RoleCreateDto dto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rôle introuvable avec l'id: " + id));

        String oldName = role.getName();
        String normalizedName = dto.getName().toUpperCase().trim();

        // Check uniqueness only if name changed
        if (!normalizedName.equals(role.getName()) && roleRepository.existsByName(normalizedName)) {
            throw new IllegalArgumentException("Un rôle avec le nom '" + normalizedName + "' existe déjà");
        }

        role.setName(normalizedName);
        role.setDescription(dto.getDescription());
        if (dto.getActif() != null) {
            role.setActif(dto.getActif());
        }

        Role saved = roleRepository.save(role);
        auditService.logUpdate("ROLE", saved.getId(),
                "Mise à jour: " + oldName + " -> " + saved.getName());
        log.info("Rôle mis à jour: {} (id={})", saved.getName(), saved.getId());
        return toDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rôle introuvable avec l'id: " + id));
        String name = role.getName();
        roleRepository.deleteById(id);
        auditService.logDelete("ROLE", id, "Suppression du rôle: " + name);
        log.info("Rôle supprimé: {} (id={})", name, id);
    }

    @Transactional
    public RoleDto toggleActive(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rôle introuvable avec l'id: " + id));
        role.setActif(!role.getActif());
        Role saved = roleRepository.save(role);
        auditService.log("TOGGLE", "ROLE", id,
                "Rôle " + saved.getName() + " -> " + (saved.getActif() ? "activé" : "désactivé"));
        return toDto(saved);
    }

    public RoleDto toDto(Role role) {
        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .actif(role.getActif())
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
                .build();
    }
}
