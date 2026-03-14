package sn.dakarterminal.dt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.dakarterminal.dt.entity.GuichetEntity;

import java.util.List;

@Repository
public interface GuichetRepository extends JpaRepository<GuichetEntity, Long> {
    List<GuichetEntity> findByServiceId(Long serviceId);
    List<GuichetEntity> findByActifTrue();
}
