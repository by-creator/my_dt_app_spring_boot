package sn.dakarterminal.dt.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.dakarterminal.dt.entity.DossierFacturation;
import sn.dakarterminal.dt.enums.StatutDossier;

import java.util.List;

@Repository
public interface DossierFacturationRepository extends JpaRepository<DossierFacturation, Long> {
    List<DossierFacturation> findByUserId(Long userId);
    Page<DossierFacturation> findByStatut(StatutDossier statut, Pageable pageable);
    List<DossierFacturation> findByStatut(StatutDossier statut);
    @Query("SELECT d FROM DossierFacturation d WHERE d.relanceProforma >= :count")
    List<DossierFacturation> findDossiersNeedingRelance(@Param("count") int count);
}
