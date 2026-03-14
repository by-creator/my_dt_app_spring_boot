package sn.dakarterminal.dt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sn.dakarterminal.dt.entity.Ticket;
import sn.dakarterminal.dt.enums.StatutTicket;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByStatutOrderByCreatedAtAsc(StatutTicket statut);
    List<Ticket> findByServiceIdAndStatut(Long serviceId, StatutTicket statut);
    Optional<Ticket> findByNumero(String numero);
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.service.id = :serviceId AND t.createdAt >= :startOfDay")
    int countTodayByService(@org.springframework.data.repository.query.Param("serviceId") Long serviceId,
                            @org.springframework.data.repository.query.Param("startOfDay") LocalDateTime startOfDay);
    List<Ticket> findByCreatedAtBetween(LocalDateTime debut, LocalDateTime fin);
}
