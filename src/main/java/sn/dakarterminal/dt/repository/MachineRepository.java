package sn.dakarterminal.dt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.dakarterminal.dt.entity.Machine;
import sn.dakarterminal.dt.enums.TypeMachine;

import java.util.List;
import java.util.Optional;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {
    Optional<Machine> findByNumeroSerie(String numeroSerie);
    List<Machine> findByType(TypeMachine type);
    List<Machine> findByService(String service);
    List<Machine> findByUtilisateur(String utilisateur);
}
