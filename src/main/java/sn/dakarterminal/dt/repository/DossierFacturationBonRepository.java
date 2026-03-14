package sn.dakarterminal.dt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.dakarterminal.dt.entity.DossierFacturationBon;

import java.util.List;
import java.util.Optional;

@Repository
public interface DossierFacturationBonRepository extends JpaRepository<DossierFacturationBon, Long> {
    Optional<DossierFacturationBon> findByDossierId(Long dossierId);
    Optional<DossierFacturationBon> findByNumeroBon(String numeroBon);
    List<DossierFacturationBon> findByValidatedAtIsNull();
}
