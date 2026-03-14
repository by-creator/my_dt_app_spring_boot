package sn.dakarterminal.dt.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ordre_approches")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdreApproche {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_oa", unique = true, length = 100)
    private String numeroOa;

    @Column(name = "navire", length = 100)
    private String navire;

    @Column(name = "armateur", length = 150)
    private String armateur;

    @Column(name = "consignataire", length = 150)
    private String consignataire;

    @Column(name = "terminal", length = 100)
    private String terminal;

    @Column(name = "quai", length = 100)
    private String quai;

    @Column(name = "voyage", length = 50)
    private String voyage;

    @Column(name = "date_arrivee_prevue")
    private LocalDate dateArriveePrevue;

    @Column(name = "date_depart_prevue")
    private LocalDate dateDepartPrevue;

    @Column(name = "date_arrivee_reelle")
    private LocalDate dateArriveeReelle;

    @Column(name = "date_depart_reel")
    private LocalDate dateDepartReel;

    @Column(name = "statut", length = 50)
    @Builder.Default
    private String statut = "PLANIFIE";

    @Column(name = "pavillon", length = 50)
    private String pavillon;

    @Column(name = "jauge", length = 50)
    private String jauge;

    @Column(name = "tirant_eau", length = 50)
    private String tirantEau;

    @Column(name = "longueur", length = 50)
    private String longueur;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
