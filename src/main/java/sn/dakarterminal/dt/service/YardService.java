package sn.dakarterminal.dt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.dakarterminal.dt.entity.Yard;
import sn.dakarterminal.dt.exception.ResourceNotFoundException;
import sn.dakarterminal.dt.repository.YardRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class YardService {

    private final YardRepository yardRepository;

    @Transactional(readOnly = true)
    public List<Yard> findAll() {
        return yardRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Yard findById(Long id) {
        return yardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Yard entry not found: " + id));
    }

    public Yard create(Yard yard) {
        return yardRepository.save(yard);
    }

    public Yard update(Long id, Yard updated) {
        Yard existing = findById(id);
        existing.setNumeroConteneur(updated.getNumeroConteneur());
        existing.setTypeConteneur(updated.getTypeConteneur());
        existing.setEtat(updated.getEtat());
        existing.setPosition(updated.getPosition());
        existing.setNavire(updated.getNavire());
        existing.setClient(updated.getClient());
        existing.setBl(updated.getBl());
        existing.setStatut(updated.getStatut());
        existing.setNotes(updated.getNotes());
        return yardRepository.save(existing);
    }

    public void delete(Long id) {
        findById(id);
        yardRepository.deleteById(id);
    }
}
