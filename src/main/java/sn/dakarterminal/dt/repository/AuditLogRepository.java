package sn.dakarterminal.dt.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.dakarterminal.dt.entity.AuditLog;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    Page<AuditLog> findByUserEmailContainingIgnoreCase(String userEmail, Pageable pageable);

    Page<AuditLog> findByAction(String action, Pageable pageable);

    Page<AuditLog> findByEntityType(String entityType, Pageable pageable);

    @Query("""
        SELECT a FROM AuditLog a
        WHERE (:userEmail IS NULL OR LOWER(a.userEmail) LIKE LOWER(CONCAT('%', :userEmail, '%')))
          AND (:action IS NULL OR a.action = :action)
          AND (:entityType IS NULL OR a.entityType = :entityType)
          AND (:dateFrom IS NULL OR a.createdAt >= :dateFrom)
          AND (:dateTo IS NULL OR a.createdAt <= :dateTo)
        ORDER BY a.createdAt DESC
    """)
    Page<AuditLog> search(
        @Param("userEmail") String userEmail,
        @Param("action") String action,
        @Param("entityType") String entityType,
        @Param("dateFrom") LocalDateTime dateFrom,
        @Param("dateTo") LocalDateTime dateTo,
        Pageable pageable
    );

    List<AuditLog> findTop10ByOrderByCreatedAtDesc();

    long countByAction(String action);

    long countByCreatedAtAfter(LocalDateTime dateTime);
}
