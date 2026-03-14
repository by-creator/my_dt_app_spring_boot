package sn.dakarterminal.dt.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import sn.dakarterminal.dt.enums.StatutDossier;

import java.time.LocalDateTime;

@Entity
@Table(name = "dossier_facturations")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DossierFacturation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rattachement_bl_id")
    private RattachementBl rattachementBl;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, columnDefinition = "VARCHAR(50)")
    @Builder.Default
    private StatutDossier statut = StatutDossier.EN_ATTENTE_VALIDATION;

    @Column(name = "date_proforma")
    private LocalDateTime dateProforma;

    @Column(name = "time_elapsed_proforma")
    private Long timeElapsedProforma;

    @Column(name = "time_elapsed_facture")
    private Long timeElapsedFacture;

    @Column(name = "time_elapsed_bon")
    private Long timeElapsedBon;

    @Column(name = "relance_proforma")
    @Builder.Default
    private Integer relanceProforma = 0;

    @Column(name = "relance_facture")
    @Builder.Default
    private Integer relanceFacture = 0;

    @Column(name = "relance_bon")
    @Builder.Default
    private Integer relanceBon = 0;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
