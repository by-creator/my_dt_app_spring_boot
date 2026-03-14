package sn.dakarterminal.dt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.dakarterminal.dt.dto.DossierFacturationDto;
import sn.dakarterminal.dt.entity.DossierFacturation;
import sn.dakarterminal.dt.entity.RattachementBl;
import sn.dakarterminal.dt.entity.User;
import sn.dakarterminal.dt.enums.StatutDossier;
import sn.dakarterminal.dt.exception.ResourceNotFoundException;
import sn.dakarterminal.dt.repository.DossierFacturationRepository;
import sn.dakarterminal.dt.repository.RattachementBlRepository;
import sn.dakarterminal.dt.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DossierFacturationService {

    private final DossierFacturationRepository dossierRepository;
    private final UserRepository userRepository;
    private final RattachementBlRepository rattachementBlRepository;
    private final EmailService emailService;

    @Transactional(readOnly = true)
    public List<DossierFacturationDto> findAll() {
        return dossierRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<DossierFacturationDto> findByStatut(StatutDossier statut, Pageable pageable) {
        return dossierRepository.findByStatut(statut, pageable).map(this::toDto);
    }

    @Transactional(readOnly = true)
    public DossierFacturationDto findById(Long id) {
        return toDto(dossierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dossier not found: " + id)));
    }

    public DossierFacturationDto create(Long userId, Long rattachementBlId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        RattachementBl bl = rattachementBlRepository.findById(rattachementBlId)
                .orElseThrow(() -> new ResourceNotFoundException("Rattachement BL not found: " + rattachementBlId));

        DossierFacturation dossier = DossierFacturation.builder()
                .user(user)
                .rattachementBl(bl)
                .statut(StatutDossier.EN_ATTENTE_VALIDATION)
                .build();

        return toDto(dossierRepository.save(dossier));
    }

    public DossierFacturationDto updateStatut(Long id, StatutDossier statut) {
        DossierFacturation dossier = dossierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dossier not found: " + id));

        dossier.setStatut(statut);

        if (statut == StatutDossier.EN_ATTENTE_PROFORMA) {
            dossier.setDateProforma(LocalDateTime.now());
        }

        DossierFacturation saved = dossierRepository.save(dossier);

        if (dossier.getUser() != null && dossier.getUser().getEmail() != null) {
            String bl = dossier.getRattachementBl() != null ? dossier.getRattachementBl().getBl() : "N/A";
            emailService.sendDossierNotification(dossier.getUser().getEmail(), bl, statut.name(), null);
        }

        return toDto(saved);
    }

    public DossierFacturationDto valider(Long id) {
        return updateStatut(id, StatutDossier.VALIDE);
    }

    public DossierFacturationDto rejeter(Long id, String motif) {
        DossierFacturation dossier = dossierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dossier not found: " + id));
        dossier.setStatut(StatutDossier.REJETE);
        DossierFacturation saved = dossierRepository.save(dossier);

        if (dossier.getUser() != null && dossier.getUser().getEmail() != null) {
            String bl = dossier.getRattachementBl() != null ? dossier.getRattachementBl().getBl() : "N/A";
            emailService.sendDossierNotification(dossier.getUser().getEmail(), bl, "REJETE", motif);
        }

        return toDto(saved);
    }

    private DossierFacturationDto toDto(DossierFacturation d) {
        return DossierFacturationDto.builder()
                .id(d.getId())
                .userId(d.getUser() != null ? d.getUser().getId() : null)
                .userEmail(d.getUser() != null ? d.getUser().getEmail() : null)
                .rattachementBlId(d.getRattachementBl() != null ? d.getRattachementBl().getId() : null)
                .numeroBl(d.getRattachementBl() != null ? d.getRattachementBl().getBl() : null)
                .statut(d.getStatut())
                .dateProforma(d.getDateProforma())
                .timeElapsedProforma(d.getTimeElapsedProforma())
                .timeElapsedFacture(d.getTimeElapsedFacture())
                .timeElapsedBon(d.getTimeElapsedBon())
                .relanceProforma(d.getRelanceProforma())
                .relanceFacture(d.getRelanceFacture())
                .relanceBon(d.getRelanceBon())
                .createdAt(d.getCreatedAt())
                .updatedAt(d.getUpdatedAt())
                .build();
    }
}
