package sn.dakarterminal.dt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.dakarterminal.dt.entity.TiersIpaki;

import java.util.List;
import java.util.Optional;

@Repository
public interface TiersIpakiRepository extends JpaRepository<TiersIpaki, Long> {
    Optional<TiersIpaki> findByCodeTiers(String codeTiers);
    List<TiersIpaki> findByActifTrue();
    List<TiersIpaki> findByNomContainingIgnoreCase(String nom);
}
