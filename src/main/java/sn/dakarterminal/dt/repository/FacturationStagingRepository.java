package sn.dakarterminal.dt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.dakarterminal.dt.entity.FacturationStaging;

import java.util.List;

@Repository
public interface FacturationStagingRepository extends JpaRepository<FacturationStaging, Long> {
    List<FacturationStaging> findByBatchId(String batchId);
    List<FacturationStaging> findByProcessedFalse();
    void deleteByBatchId(String batchId);
}
