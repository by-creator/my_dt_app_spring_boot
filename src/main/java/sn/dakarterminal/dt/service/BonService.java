package sn.dakarterminal.dt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sn.dakarterminal.dt.entity.DossierFacturation;
import sn.dakarterminal.dt.entity.DossierFacturationBon;
import sn.dakarterminal.dt.enums.StatutDossier;
import sn.dakarterminal.dt.exception.ResourceNotFoundException;
import sn.dakarterminal.dt.repository.DossierFacturationBonRepository;
import sn.dakarterminal.dt.repository.DossierFacturationRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BonService {

    private final DossierFacturationBonRepository bonRepository;
    private final DossierFacturationRepository dossierRepository;
    private final FileStorageService fileStorageService;

    public DossierFacturationBon upload(Long dossierId, MultipartFile file, String numeroBon) throws IOException {
        DossierFacturation dossier = dossierRepository.findById(dossierId)
                .orElseThrow(() -> new ResourceNotFoundException("Dossier not found: " + dossierId));

        String filePath = fileStorageService.store(file, "bons/" + dossierId);

        DossierFacturationBon bon = DossierFacturationBon.builder()
                .dossier(dossier)
                .filePath(filePath)
                .numeroBon(numeroBon)
                .build();

        return bonRepository.save(bon);
    }

    public DossierFacturationBon validate(Long bonId) {
        DossierFacturationBon bon = bonRepository.findById(bonId)
                .orElseThrow(() -> new ResourceNotFoundException("Bon not found: " + bonId));

        bon.setValidatedAt(LocalDateTime.now());

        DossierFacturation dossier = bon.getDossier();
        if (dossier.getDateProforma() != null) {
            long elapsed = ChronoUnit.SECONDS.between(dossier.getDateProforma(), LocalDateTime.now());
            dossier.setTimeElapsedBon(elapsed);
        }
        dossier.setStatut(StatutDossier.CLOTURE);
        dossierRepository.save(dossier);

        return bonRepository.save(bon);
    }

    public DossierFacturationBon findByDossierId(Long dossierId) {
        return bonRepository.findByDossierId(dossierId)
                .orElseThrow(() -> new ResourceNotFoundException("Bon not found for dossier: " + dossierId));
    }
}
