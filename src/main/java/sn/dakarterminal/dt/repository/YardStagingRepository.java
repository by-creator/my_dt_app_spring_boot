package sn.dakarterminal.dt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.dakarterminal.dt.entity.YardStaging;

import java.util.List;

@Repository
public interface YardStagingRepository extends JpaRepository<YardStaging, Long> {
    List<YardStaging> findByBatchId(String batchId);
    List<YardStaging> findByProcessedFalse();
    void deleteByBatchId(String batchId);
}
