package sn.dakarterminal.dt.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.dakarterminal.dt.entity.Facturation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FacturationRepository extends JpaRepository<Facturation, Long> {
    Optional<Facturation> findByNumeroBl(String numeroBl);
    Optional<Facturation> findByNumeroFacture(String numeroFacture);
    Page<Facturation> findByClientContainingIgnoreCase(String client, Pageable pageable);
    List<Facturation> findByDateFactureBetween(LocalDate debut, LocalDate fin);
    @Query("SELECT f FROM Facturation f WHERE f.statut = :statut AND f.dateFacture BETWEEN :debut AND :fin")
    List<Facturation> findByStatutAndPeriode(@Param("statut") String statut, @Param("debut") LocalDate debut, @Param("fin") LocalDate fin);
}
