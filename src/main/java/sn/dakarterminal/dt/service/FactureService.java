package sn.dakarterminal.dt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sn.dakarterminal.dt.entity.DossierFacturation;
import sn.dakarterminal.dt.entity.DossierFacturationFacture;
import sn.dakarterminal.dt.enums.StatutDossier;
import sn.dakarterminal.dt.exception.ResourceNotFoundException;
import sn.dakarterminal.dt.repository.DossierFacturationFactureRepository;
import sn.dakarterminal.dt.repository.DossierFacturationRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FactureService {

    private final DossierFacturationFactureRepository factureRepository;
    private final DossierFacturationRepository dossierRepository;
    private final FileStorageService fileStorageService;

    public DossierFacturationFacture upload(Long dossierId, MultipartFile file, String numeroFacture) throws IOException {
        DossierFacturation dossier = dossierRepository.findById(dossierId)
                .orElseThrow(() -> new ResourceNotFoundException("Dossier not found: " + dossierId));

        String filePath = fileStorageService.store(file, "factures/" + dossierId);

        DossierFacturationFacture facture = DossierFacturationFacture.builder()
                .dossier(dossier)
                .filePath(filePath)
                .numeroFacture(numeroFacture)
                .build();

        dossier.setStatut(StatutDossier.EN_ATTENTE_BAD);
        dossierRepository.save(dossier);

        return factureRepository.save(facture);
    }

    public DossierFacturationFacture validate(Long factureId) {
        DossierFacturationFacture facture = factureRepository.findById(factureId)
                .orElseThrow(() -> new ResourceNotFoundException("Facture not found: " + factureId));

        facture.setValidatedAt(LocalDateTime.now());

        DossierFacturation dossier = facture.getDossier();
        if (dossier.getDateProforma() != null) {
            long elapsed = ChronoUnit.SECONDS.between(dossier.getDateProforma(), LocalDateTime.now());
            dossier.setTimeElapsedFacture(elapsed);
            dossierRepository.save(dossier);
        }

        return factureRepository.save(facture);
    }

    public DossierFacturationFacture findByDossierId(Long dossierId) {
        return factureRepository.findByDossierId(dossierId)
                .orElseThrow(() -> new ResourceNotFoundException("Facture not found for dossier: " + dossierId));
    }
}
