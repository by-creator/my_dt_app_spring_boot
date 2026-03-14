package sn.dakarterminal.dt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.dakarterminal.dt.entity.RattachementBl;

import java.util.List;
import java.util.Optional;

@Repository
public interface RattachementBlRepository extends JpaRepository<RattachementBl, Long> {
    List<RattachementBl> findByUserId(Long userId);
    Optional<RattachementBl> findByBl(String bl);
    List<RattachementBl> findByStatut(String statut);
    boolean existsByBlAndEmail(String bl, String email);
}
