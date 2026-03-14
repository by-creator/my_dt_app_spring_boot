package sn.dakarterminal.dt.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sn.dakarterminal.dt.repository.OrdreApprocheStagingRepository;
import sn.dakarterminal.dt.entity.OrdreApproche;
import sn.dakarterminal.dt.entity.OrdreApprocheStaging;
import sn.dakarterminal.dt.repository.OrdreApprocheRepository;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImportOrdreApprocheCsvJob {

    private final OrdreApprocheStagingRepository stagingRepository;
    private final OrdreApprocheRepository ordreApprocheRepository;

    @Scheduled(fixedDelay = 300000) // every 5 minutes
    public void processUnprocessedStagings() {
        List<OrdreApprocheStaging> stagings = stagingRepository.findByProcessedFalse();
        if (stagings.isEmpty()) {
            return;
        }

        log.info("Processing {} unprocessed ordre approche stagings", stagings.size());

        for (OrdreApprocheStaging staging : stagings) {
            try {
                OrdreApproche oa = OrdreApproche.builder()
                        .numeroOa(staging.getNumeroOa())
                        .navire(staging.getNavire())
                        .armateur(staging.getArmateur())
                        .consignataire(staging.getConsignataire())
                        .terminal(staging.getTerminal())
                        .quai(staging.getQuai())
                        .voyage(staging.getVoyage())
                        .dateArriveePrevue(staging.getDateArriveePrevue())
                        .dateDepartPrevue(staging.getDateDepartPrevue())
                        .statut(staging.getStatut() != null ? staging.getStatut() : "PLANIFIE")
                        .build();

                ordreApprocheRepository.save(oa);
                staging.setProcessed(true);
                stagingRepository.save(staging);
            } catch (Exception e) {
                staging.setImportErrors(e.getMessage());
                stagingRepository.save(staging);
                log.error("Error processing staging {}: {}", staging.getId(), e.getMessage());
            }
        }

        log.info("Completed processing ordre approche stagings");
    }
}
