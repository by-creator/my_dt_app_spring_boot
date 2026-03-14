package sn.dakarterminal.dt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.dakarterminal.dt.entity.OrdreApproche;
import sn.dakarterminal.dt.repository.OrdreApprocheRepository;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanificationService {

    private final OrdreApprocheRepository ordreApprocheRepository;

    public List<OrdreApproche> getPlanning(LocalDate debut, LocalDate fin) {
        return ordreApprocheRepository.findByDateArriveePrevueBetween(debut, fin);
    }

    public List<OrdreApproche> getByStatut(String statut) {
        return ordreApprocheRepository.findByStatut(statut);
    }
}
