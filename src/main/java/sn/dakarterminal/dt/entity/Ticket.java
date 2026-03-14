package sn.dakarterminal.dt.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import sn.dakarterminal.dt.enums.StatutTicket;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private ServiceEntity service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id")
    private Agent agent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guichet_id")
    private GuichetEntity guichet;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, length = 50, columnDefinition = "VARCHAR(50)")
    @Builder.Default
    private StatutTicket statut = StatutTicket.EN_ATTENTE;

    @Column(name = "numero", nullable = false, length = 20)
    private String numero;

    @Column(name = "nom_client", length = 150)
    private String nomClient;

    @Column(name = "motif", columnDefinition = "TEXT")
    private String motif;

    @Column(name = "called_at")
    private LocalDateTime calledAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    @Column(name = "processing_time")
    private Long processingTime;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
