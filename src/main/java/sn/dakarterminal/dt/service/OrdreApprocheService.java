package sn.dakarterminal.dt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.dakarterminal.dt.entity.OrdreApproche;
import sn.dakarterminal.dt.exception.ResourceNotFoundException;
import sn.dakarterminal.dt.repository.OrdreApprocheRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrdreApprocheService {

    private final OrdreApprocheRepository ordreApprocheRepository;

    @Transactional(readOnly = true)
    public List<OrdreApproche> findAll() {
        return ordreApprocheRepository.findAll();
    }

    @Transactional(readOnly = true)
    public OrdreApproche findById(Long id) {
        return ordreApprocheRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ordre d'approche not found: " + id));
    }

    public OrdreApproche create(OrdreApproche ordreApproche) {
        return ordreApprocheRepository.save(ordreApproche);
    }

    public OrdreApproche update(Long id, OrdreApproche updated) {
        OrdreApproche existing = findById(id);
        existing.setNavire(updated.getNavire());
        existing.setArmateur(updated.getArmateur());
        existing.setConsignataire(updated.getConsignataire());
        existing.setTerminal(updated.getTerminal());
        existing.setQuai(updated.getQuai());
        existing.setVoyage(updated.getVoyage());
        existing.setDateArriveePrevue(updated.getDateArriveePrevue());
        existing.setDateDepartPrevue(updated.getDateDepartPrevue());
        existing.setStatut(updated.getStatut());
        existing.setNotes(updated.getNotes());
        return ordreApprocheRepository.save(existing);
    }

    public void delete(Long id) {
        findById(id);
        ordreApprocheRepository.deleteById(id);
    }
}
