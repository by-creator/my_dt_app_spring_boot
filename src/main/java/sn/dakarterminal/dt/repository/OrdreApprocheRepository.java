package sn.dakarterminal.dt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.dakarterminal.dt.entity.OrdreApproche;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrdreApprocheRepository extends JpaRepository<OrdreApproche, Long> {
    Optional<OrdreApproche> findByNumeroOa(String numeroOa);
    List<OrdreApproche> findByStatut(String statut);
    List<OrdreApproche> findByDateArriveePrevueBetween(LocalDate debut, LocalDate fin);
    List<OrdreApproche> findByNavireContainingIgnoreCase(String navire);
}
