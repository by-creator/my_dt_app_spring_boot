package sn.dakarterminal.dt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.dakarterminal.dt.entity.Machine;
import sn.dakarterminal.dt.enums.TypeMachine;
import sn.dakarterminal.dt.exception.ResourceNotFoundException;
import sn.dakarterminal.dt.repository.MachineRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MachineService {

    private final MachineRepository machineRepository;

    @Transactional(readOnly = true)
    public List<Machine> findAll() {
        return machineRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Machine findById(Long id) {
        return machineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Machine not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Machine> findByType(TypeMachine type) {
        return machineRepository.findByType(type);
    }

    public Machine create(Machine machine) {
        return machineRepository.save(machine);
    }

    public Machine update(Long id, Machine updated) {
        Machine existing = findById(id);
        existing.setNumeroSerie(updated.getNumeroSerie());
        existing.setModele(updated.getModele());
        existing.setType(updated.getType());
        existing.setUtilisateur(updated.getUtilisateur());
        existing.setService(updated.getService());
        existing.setSite(updated.getSite());
        existing.setMarque(updated.getMarque());
        existing.setEtat(updated.getEtat());
        existing.setNotes(updated.getNotes());
        return machineRepository.save(existing);
    }

    public void delete(Long id) {
        findById(id);
        machineRepository.deleteById(id);
    }
}
