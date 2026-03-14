package sn.dakarterminal.dt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.dakarterminal.dt.entity.Yard;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface YardRepository extends JpaRepository<Yard, Long> {
    Optional<Yard> findByNumeroConteneur(String numeroConteneur);
    List<Yard> findByStatut(String statut);
    List<Yard> findByNavire(String navire);
    List<Yard> findByDateArriveeBetween(LocalDate debut, LocalDate fin);
}
