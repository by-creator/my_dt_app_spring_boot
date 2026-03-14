package sn.dakarterminal.dt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.dakarterminal.dt.entity.DossierFacturationFacture;

import java.util.List;
import java.util.Optional;

@Repository
public interface DossierFacturationFactureRepository extends JpaRepository<DossierFacturationFacture, Long> {
    Optional<DossierFacturationFacture> findByDossierId(Long dossierId);
    Optional<DossierFacturationFacture> findByNumeroFacture(String numeroFacture);
    List<DossierFacturationFacture> findByValidatedAtIsNull();
}
