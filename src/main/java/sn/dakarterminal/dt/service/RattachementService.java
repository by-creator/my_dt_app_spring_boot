package sn.dakarterminal.dt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.dakarterminal.dt.dto.RattachementBlDto;
import sn.dakarterminal.dt.entity.RattachementBl;
import sn.dakarterminal.dt.entity.User;
import sn.dakarterminal.dt.exception.ResourceNotFoundException;
import sn.dakarterminal.dt.repository.RattachementBlRepository;
import sn.dakarterminal.dt.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RattachementService {

    private final RattachementBlRepository rattachementBlRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Transactional(readOnly = true)
    public List<RattachementBlDto> findAll() {
        return rattachementBlRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RattachementBlDto> findByUserId(Long userId) {
        return rattachementBlRepository.findByUserId(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RattachementBlDto findById(Long id) {
        return toDto(rattachementBlRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rattachement BL not found with id: " + id)));
    }

    public RattachementBlDto create(RattachementBlDto dto) {
        User user = null;
        if (dto.getUserId() != null) {
            user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + dto.getUserId()));
        }

        RattachementBl rattachement = RattachementBl.builder()
                .user(user)
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .email(dto.getEmail())
                .bl(dto.getBl())
                .compte(dto.getCompte())
                .statut("EN_ATTENTE")
                .build();

        RattachementBl saved = rattachementBlRepository.save(rattachement);
        log.info("Rattachement BL created for BL: {}", dto.getBl());
        return toDto(saved);
    }

    public RattachementBlDto validate(Long id) {
        RattachementBl rattachement = rattachementBlRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rattachement BL not found: " + id));

        LocalDateTime now = LocalDateTime.now();
        long elapsed = ChronoUnit.SECONDS.between(rattachement.getCreatedAt(), now);

        rattachement.setStatut("VALIDE");
        rattachement.setTimeElapsed(elapsed);
        RattachementBl saved = rattachementBlRepository.save(rattachement);

        if (rattachement.getEmail() != null) {
            emailService.sendRattachementNotification(rattachement.getEmail(), rattachement.getBl(), "VALIDE");
        }

        return toDto(saved);
    }

    public RattachementBlDto reject(Long id, String motif) {
        RattachementBl rattachement = rattachementBlRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rattachement BL not found: " + id));

        rattachement.setStatut("REJETE");
        RattachementBl saved = rattachementBlRepository.save(rattachement);

        if (rattachement.getEmail() != null) {
            emailService.sendRattachementNotification(rattachement.getEmail(), rattachement.getBl(), "REJETE");
        }

        return toDto(saved);
    }

    public void delete(Long id) {
        rattachementBlRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rattachement BL not found: " + id));
        rattachementBlRepository.deleteById(id);
    }

    private RattachementBlDto toDto(RattachementBl r) {
        return RattachementBlDto.builder()
                .id(r.getId())
                .userId(r.getUser() != null ? r.getUser().getId() : null)
                .nom(r.getNom())
                .prenom(r.getPrenom())
                .email(r.getEmail())
                .bl(r.getBl())
                .compte(r.getCompte())
                .statut(r.getStatut())
                .timeElapsed(r.getTimeElapsed())
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }
}
