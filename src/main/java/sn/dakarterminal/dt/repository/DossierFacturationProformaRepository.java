package sn.dakarterminal.dt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.dakarterminal.dt.entity.DossierFacturationProforma;

import java.util.List;
import java.util.Optional;

@Repository
public interface DossierFacturationProformaRepository extends JpaRepository<DossierFacturationProforma, Long> {
    Optional<DossierFacturationProforma> findByDossierId(Long dossierId);
    List<DossierFacturationProforma> findByValidatedAtIsNull();
}
