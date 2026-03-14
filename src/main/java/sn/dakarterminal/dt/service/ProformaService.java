package sn.dakarterminal.dt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sn.dakarterminal.dt.entity.DossierFacturation;
import sn.dakarterminal.dt.entity.DossierFacturationProforma;
import sn.dakarterminal.dt.enums.StatutDossier;
import sn.dakarterminal.dt.exception.ResourceNotFoundException;
import sn.dakarterminal.dt.repository.DossierFacturationProformaRepository;
import sn.dakarterminal.dt.repository.DossierFacturationRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProformaService {

    private final DossierFacturationProformaRepository proformaRepository;
    private final DossierFacturationRepository dossierRepository;
    private final FileStorageService fileStorageService;
    private final EmailService emailService;

    public DossierFacturationProforma upload(Long dossierId, MultipartFile file) throws IOException {
        DossierFacturation dossier = dossierRepository.findById(dossierId)
                .orElseThrow(() -> new ResourceNotFoundException("Dossier not found: " + dossierId));

        String filePath = fileStorageService.store(file, "proformas/" + dossierId);

        DossierFacturationProforma proforma = DossierFacturationProforma.builder()
                .dossier(dossier)
                .filePath(filePath)
                .generatedAt(LocalDateTime.now())
                .build();

        dossier.setStatut(StatutDossier.EN_ATTENTE_FACTURE);
        dossierRepository.save(dossier);

        return proformaRepository.save(proforma);
    }

    public DossierFacturationProforma validate(Long proformaId) {
        DossierFacturationProforma proforma = proformaRepository.findById(proformaId)
                .orElseThrow(() -> new ResourceNotFoundException("Proforma not found: " + proformaId));

        proforma.setValidatedAt(LocalDateTime.now());
        DossierFacturation dossier = proforma.getDossier();

        if (dossier.getDateProforma() != null) {
            long elapsed = ChronoUnit.SECONDS.between(dossier.getDateProforma(), LocalDateTime.now());
            dossier.setTimeElapsedProforma(elapsed);
        }

        dossierRepository.save(dossier);
        return proformaRepository.save(proforma);
    }

    public DossierFacturationProforma findByDossierId(Long dossierId) {
        return proformaRepository.findByDossierId(dossierId)
                .orElseThrow(() -> new ResourceNotFoundException("Proforma not found for dossier: " + dossierId));
    }
}
