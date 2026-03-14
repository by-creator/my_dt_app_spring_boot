package sn.dakarterminal.dt.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sn.dakarterminal.dt.entity.FacturationStaging;
import sn.dakarterminal.dt.entity.Facturation;
import sn.dakarterminal.dt.repository.FacturationRepository;
import sn.dakarterminal.dt.repository.FacturationStagingRepository;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsolidateFacturationJob {

    private final FacturationStagingRepository stagingRepository;
    private final FacturationRepository facturationRepository;

    @Scheduled(cron = "0 0 2 * * *") // Every day at 2 AM
    public void consolidate() {
        List<FacturationStaging> unprocessed = stagingRepository.findByProcessedFalse();
        if (unprocessed.isEmpty()) {
            return;
        }

        log.info("Consolidating {} facturation staging records", unprocessed.size());
        int success = 0;
        int failed = 0;

        for (FacturationStaging staging : unprocessed) {
            try {
                Facturation facturation = Facturation.builder()
                        .numeroBl(staging.getNumeroBl())
                        .numeroFacture(staging.getNumeroFacture())
                        .numeroProforma(staging.getNumeroProforma())
                        .navire(staging.getNavire())
                        .armateur(staging.getArmateur())
                        .consignataire(staging.getConsignataire())
                        .client(staging.getClient())
                        .codeClient(staging.getCodeClient())
                        .nombreConteneurs(staging.getNombreConteneurs())
                        .typeConteneur(staging.getTypeConteneur())
                        .poids(staging.getPoids())
                        .montantHt(staging.getMontantHt())
                        .montantTva(staging.getMontantTva())
                        .montantTtc(staging.getMontantTtc())
                        .dateArrivee(staging.getDateArrivee())
                        .dateFacture(staging.getDateFacture())
                        .statut(staging.getStatut())
                        .build();

                facturationRepository.save(facturation);
                staging.setProcessed(true);
                stagingRepository.save(staging);
                success++;
            } catch (Exception e) {
                staging.setImportErrors(e.getMessage());
                stagingRepository.save(staging);
                log.error("Failed to consolidate staging {}: {}", staging.getId(), e.getMessage());
                failed++;
            }
        }

        log.info("Facturation consolidation complete. Success: {}, Failed: {}", success, failed);
    }
}
