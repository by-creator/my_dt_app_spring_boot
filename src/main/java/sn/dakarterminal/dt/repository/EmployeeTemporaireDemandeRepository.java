package sn.dakarterminal.dt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.dakarterminal.dt.entity.EmployeeTemporaireDemande;

import java.util.List;

@Repository
public interface EmployeeTemporaireDemandeRepository extends JpaRepository<EmployeeTemporaireDemande, Long> {
    List<EmployeeTemporaireDemande> findByUserId(Long userId);
    List<EmployeeTemporaireDemande> findByStatut(String statut);
}
