package sn.dakarterminal.dt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.dakarterminal.dt.entity.EdiRecord;

import java.util.List;

@Repository
public interface EdiRecordRepository extends JpaRepository<EdiRecord, Long> {
    List<EdiRecord> findByProcessedFalse();
    List<EdiRecord> findByMessageType(String messageType);
    List<EdiRecord> findByStatut(String statut);
}
