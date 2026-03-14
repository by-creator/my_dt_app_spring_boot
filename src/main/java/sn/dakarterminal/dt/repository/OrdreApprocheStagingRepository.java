package sn.dakarterminal.dt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.dakarterminal.dt.entity.OrdreApprocheStaging;

import java.util.List;

@Repository
public interface OrdreApprocheStagingRepository extends JpaRepository<OrdreApprocheStaging, Long> {
    List<OrdreApprocheStaging> findByBatchId(String batchId);
    List<OrdreApprocheStaging> findByProcessedFalse();
    void deleteByBatchId(String batchId);
}
