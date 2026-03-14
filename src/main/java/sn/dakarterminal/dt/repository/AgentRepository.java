package sn.dakarterminal.dt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.dakarterminal.dt.entity.Agent;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    List<Agent> findByServiceId(Long serviceId);
    List<Agent> findByActifTrue();
    List<Agent> findByGuichetId(Long guichetId);
    Optional<Agent> findFirstByGuichetIdAndActifTrue(Long guichetId);
}
