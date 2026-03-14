package sn.dakarterminal.dt.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "rapport_suivi_detail_facturations")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RapportSuiviDetailFacturation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_bl", length = 100)
    private String numeroBl;

    @Column(name = "client", length = 150)
    private String client;

    @Column(name = "navire", length = 100)
    private String navire;

    @Column(name = "date_proforma")
    private LocalDate dateProforma;

    @Column(name = "date_facture")
    private LocalDate dateFacture;

    @Column(name = "date_bon")
    private LocalDate dateBon;

    @Column(name = "montant_ttc", precision = 15, scale = 2)
    private BigDecimal montantTtc;

    @Column(name = "statut_dossier", length = 50)
    private String statutDossier;

    @Column(name = "delai_proforma")
    private Long delaiProforma;

    @Column(name = "delai_facture")
    private Long delaiFacture;

    @Column(name = "delai_bon")
    private Long delaiBon;

    @Column(name = "rapport_date")
    private LocalDate rapportDate;

    @Column(name = "generated_by")
    private Long generatedBy;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
